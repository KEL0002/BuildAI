package de.kel0002.buildai.cmd;

import de.kel0002.buildai.BuildAI;
import de.kel0002.buildai.Feedback;
import de.kel0002.buildai.util.ConfigManager;
import de.kel0002.buildai.util.RequestHandler;
import de.kel0002.buildai.util.ResponseFormatter;
import de.kel0002.buildai.util.WorldEditUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static de.kel0002.buildai.Feedback.sendFeedback;
import static de.kel0002.buildai.util.ConfigManager.replaceInDictionary;

public class Generate implements CommandExecutor {



    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(commandSender instanceof Player)) {
            sendFeedback(commandSender, "error.not_a_player");
            return false;
        }
        Player player = (Player) commandSender;

        boolean coordinates_provided = true;
        if (args.length > 7){
            for (int i = 1; i < 7; i++) {
                if (!isInteger(args[i])) {
                    coordinates_provided = false;
                    break;
                }
            }
        } else {
            coordinates_provided = false;
        }

        Location pos1;
        Location pos2;

        if (coordinates_provided){
            Location[] normalised_locations = normalizeLocations(
            new Location(player.getWorld(), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])),
            new Location(player.getWorld(), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6])));

            pos1 = normalised_locations[0];
            pos2 = normalised_locations[1];
        } else {
            pos1 = WorldEditUtil.getPos1(player);
            pos2 = WorldEditUtil.getPos2(player);
        }


        if (pos1 == null || pos2 == null) {
            sendFeedback(commandSender, "error.selection");
            return false;
        }

        Map<String, Object> input_and_overwrites = get_input_and_overwrites(args);
        HashMap<String, String> overwrites;
        String input;

        input =  input_and_overwrites.get("input").toString();
        overwrites = (HashMap<String, String>) input_and_overwrites.get("overwrites");

        String model_preset = args[0];


        return performGeneration_async(player,
                pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ(),
                pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ(),
                input,
                model_preset,
                overwrites);
    }

    public Map<String, Object> get_input_and_overwrites(String[] args){
        HashMap<String, String> overwrites = new HashMap<>();
        StringBuilder input = new StringBuilder();

        boolean isFirstArgument = true;

        for (String string : args) {
            if (isFirstArgument) {
                isFirstArgument = false;
                continue;
            }
            if (string.contains("=")) {
                String[] parts = string.split("=", 2);
                overwrites.put("%" + parts[0] + "%", parts[1]);
            } else {
                if (input.length() > 0) {
                    input.append(" ");
                }
                input.append(string);
            }
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("overwrites", overwrites);
        result.put("input", input.toString());

        return result;
    }




    public boolean performGeneration_async(Player player, int x1, int y1, int z1, int x2, int y2, int z2, String input, String model_preset, HashMap<String, String> payload_overwrites){
        new BukkitRunnable() {
            @Override
            public void run() {
                    performGeneration(player, x1, y1, z1,
                    x2, y2, z2,
                    input, model_preset, payload_overwrites);
                }
            }.runTaskAsynchronously(BuildAI.getInstance());
        return true;
    }



    public void performGeneration(Player player, int x1, int y1, int z1, int x2, int y2, int z2, String input, String model_preset, HashMap<String, String> payload_overwrites){
        final String prompt = BuildAI.getInstance().getConfig().getString("prompt");
        if (prompt == null){
            sendFeedback(player,"error.config.prompt");
            return;
        }

        final boolean use_relative_cords = BuildAI.getInstance().getConfig().getBoolean("relative_coordinates");

        BukkitTask actionBarTask = runActionBarTask("wait.generating.actionbar", player);




        sendFeedback(player,"info.structure", input);

        Dictionary<String, Object> payload = ConfigManager.getPayload(model_preset);


        if (payload == null){
            sendFeedback(player, "error.model_preset_payload");
            actionBarTask.cancel();
            return;
        }

        do_payload_replacements(payload, use_relative_cords, payload_overwrites, prompt, input, x1, y1, z1, x2, y2, z2);

        if (BuildAI.getInstance().getConfig().getBoolean("sendpayload")){
            sendFeedback(player, "info.payload", payload.toString());
        }


        List<String> unset_vars = ConfigManager.getVarsinDictionary(payload);
        if (unset_vars != null){
            sendFeedback(player, "error.unset_vars", unset_vars.toString());
            actionBarTask.cancel();
            return;
        }

        String endpoint = ConfigManager.getUrl(model_preset);

        String response_all = RequestHandler.dorequest(endpoint, payload);


        if (response_all == null){
            sendFeedback(player, "error.request");
            actionBarTask.cancel();
            return;
        }

        String response = ResponseFormatter.extractResponseField(response_all);
        if (response == null){
            sendFeedback(player, "error.request.extract", response_all);
            actionBarTask.cancel();
            return;
        }

        ArrayList<String> response_array = ResponseFormatter.extractResponseLines(response);

        actionBarTask.cancel();

        BukkitTask actionBarTask2 = runActionBarTask("wait.placing.actionbar", player);

        ArrayList<String> finished_cmd_list = get_finished_cmd_list(response_array, player);

        sendFeedback(player, "wait.execution");

        scheduleBuildTasks(finished_cmd_list, use_relative_cords, player, x1, y1, z1);


        int build_delay = BuildAI.getInstance().getConfig().getInt("build_delay");
        Bukkit.getScheduler().runTaskLater(BuildAI.getInstance(), () -> {
            sendFeedback(player, "done.done");
            actionBarTask2.cancel();
            Feedback.clearActionBar(player);
            }, ((long) finished_cmd_list.size()*build_delay));
    }

    public void scheduleBuildTasks(ArrayList<String> finished_cmd_list, boolean use_relative_cords, Player player,
                                   int x1, int y1, int z1){

        int build_delay = BuildAI.getInstance().getConfig().getInt("build_delay");

        Bukkit.getScheduler().runTask(BuildAI.getInstance(), () -> {
            for (int i = 0; i < finished_cmd_list.size(); i++) {
                String cmd = finished_cmd_list.get(i);
                int delay = i*build_delay;


                Bukkit.getScheduler().runTaskLater(BuildAI.getInstance(), () -> {
                    if (cmd.startsWith("setblock")){
                        perform_setblock(cmd, player, use_relative_cords,
                                x1, y1, z1);

                    } else if (cmd.startsWith("fill")){
                        perform_fill(cmd, player, use_relative_cords,
                                x1, y1, z1);
                    }
                    }, delay);
            }
        });
    }

    public void perform_fill(String cmd, Player player, boolean use_relative_cords,
                                int x1, int y1, int z1){
        String[] parts = cmd.split("\\s+");
        int cx = Integer.parseInt(parts[1]);
        int cy = Integer.parseInt(parts[2]);
        int cz = Integer.parseInt(parts[3]);

        int cx2 = Integer.parseInt(parts[4]);
        int cy2 = Integer.parseInt(parts[5]);
        int cz2 = Integer.parseInt(parts[6]);

        if (use_relative_cords){
            cx = cx + x1;
            cy = cy + y1;
            cz = cz + z1;

            cx2 = cx2 + x1;
            cy2 = cy2 + y1;
            cz2 = cz2 + z1;
        }
        String blockstring = parts[7];
        boolean success = false;
        try{
        success = fillBlocks(player.getWorld(), cx, cy, cz, cx2, cy2, cz2 ,blockstring);
        } catch (Exception ignored){}
        if(!success){sendFeedback(player, "error.fill", cmd);}
    }


   public void perform_setblock(String cmd, Player player, boolean use_relative_cords,
                                int x1, int y1, int z1){
        String[] parts = cmd.split("\\s+");

        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        int z = Integer.parseInt(parts[3]);

        if (use_relative_cords){
            x = x + x1;
            y = y + y1;
            z = z + z1;
        }
        String blockstring = parts[4];
        boolean success = false;
        try {
            success = setblock(player.getWorld(), x, y, z, blockstring);
        } catch (Exception ignored){}

        if(!success){sendFeedback(player, "error.setblock", cmd);}
   }



    public ArrayList<String> get_finished_cmd_list(ArrayList<String> response_array, Player player){
        ArrayList<String> finished_cmd_list = new ArrayList<>();
        boolean sendresponse = BuildAI.getInstance().getConfig().getBoolean("sendresponse");
        if (sendresponse) sendFeedback(player, "info.response.header");

        ArrayList<String> allowedPrefixes = new ArrayList<>();
        allowedPrefixes.add("setblock");
        allowedPrefixes.add("fill");

        for (String line : response_array) {
            boolean correct_prefix = false;

            line = line.replace("sb ", "setblock ");

            for (String prefix : allowedPrefixes) {
                if (line.startsWith(prefix)) {
                    if (sendresponse) sendFeedback(player, "info.response.content.correct", line);
                    finished_cmd_list.add(line);
                    correct_prefix = true;
                    break;
                }
            }
            if (!correct_prefix && sendresponse) sendFeedback(player, "info.response.content.incorrect", line);
        }
        return finished_cmd_list;
    }



    public static void do_payload_replacements(Dictionary<String, Object> payload, boolean use_relative_cords, HashMap<String, String> payload_overwrites,
                                                                     String prompt, String input,
                                                                     int x1, int y1, int z1,
                                                                     int x2, int y2, int z2){

        replaceInDictionary(payload, "%prompt%", prompt);
        replaceInDictionary(payload, "%input%", input);

        Random rand = new Random();
        replaceInDictionary(payload, "%seed%", String.valueOf(rand.nextInt(100000)));


        if (use_relative_cords){
            int size_x = x2 - x1;
            int size_y = y2 - y1;
            int size_z  = z2 - z1;
            replaceInDictionary(payload, "%X1%", String.valueOf(0));
            replaceInDictionary(payload, "%Y1%", String.valueOf(0));
            replaceInDictionary(payload, "%Z1%", String.valueOf(0));

            replaceInDictionary(payload, "%X2%", String.valueOf(size_x));
            replaceInDictionary(payload, "%Y2%", String.valueOf(size_y));
            replaceInDictionary(payload, "%Z2%", String.valueOf(size_z));

        } else {
            replaceInDictionary(payload, "%X1%", String.valueOf(x1));
            replaceInDictionary(payload, "%Y1%", String.valueOf(y1));
            replaceInDictionary(payload, "%Z1%", String.valueOf(z1));
            replaceInDictionary(payload, "%X2%", String.valueOf(x2));
            replaceInDictionary(payload, "%Y2%", String.valueOf(y2));
            replaceInDictionary(payload, "%Z2%", String.valueOf(z2));
        }


        for (String key : payload_overwrites.keySet()){
            replaceInDictionary(payload, key, payload_overwrites.get(key));
        }
    }


    public static Material find_block_by_string(String blockString) {
        if (blockString == null || blockString.trim().isEmpty()) {
            return null;
        }
        Material material;
        try {
            material = Material.valueOf(blockString.toUpperCase());
            if (material.isBlock()) return material;
        } catch (IllegalArgumentException ignored) {}

        try {
            material = Material.valueOf(blockString.toUpperCase().replace("MINECRAFT:", ""));
            if (material.isBlock()) return material;
        } catch (IllegalArgumentException ignored) {}

        try {
            material = Material.valueOf(blockString.toUpperCase().replace(" ", "_"));
            if (material.isBlock()) return material;
        } catch (IllegalArgumentException ignored) {}

        try {
            material = Material.valueOf(blockString.toUpperCase() + "_BLOCK");
            if (material.isBlock()) return material;
        } catch (IllegalArgumentException ignored) {}

        return null;
    }


    public static boolean setblock(World world, int x, int y, int z, String blockstring){
        Material material = find_block_by_string(blockstring);
        if (!(material == null) && material.isBlock()) {
            Block block = world.getBlockAt(x, y, z);
            block.setType(material);
            return true;
        }
        return false;
    }

    public static boolean fillBlocks(World world, int x1, int y1, int z1, int x2, int y2, int z2, String blockstring) {
        Material material = find_block_by_string(blockstring);

        if (material == null || !material.isBlock()) {
            return false;
        }

        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(material);
                }
            }
        }
        return true;
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Location[] normalizeLocations(Location pos1, Location pos2) {
        double minX = Math.min(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());

        double maxX = Math.max(pos1.getX(), pos2.getX());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        Location normalizedPos1 = new Location(pos1.getWorld(), minX, minY, minZ);
        Location normalizedPos2 = new Location(pos2.getWorld(), maxX, maxY, maxZ);

        return new Location[]{normalizedPos1, normalizedPos2};
    }



    public BukkitTask runActionBarTask(String textname, Player player){
        return new BukkitRunnable() {
            int dots = 1;
            @Override
            public void run() {
                StringBuilder ds = new StringBuilder();
                for (int i = 0; i < dots; i++) {
                    ds.append(".");
                }
                Feedback.sendActionBarFeedback(player, textname, ds.toString());
                dots ++;
                if (dots == 4) dots = 1;
            }
        }.runTaskTimer(BuildAI.getInstance(), 0L, 10L);
    }

}
