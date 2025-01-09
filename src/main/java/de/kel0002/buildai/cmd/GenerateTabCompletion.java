package de.kel0002.buildai.cmd;

import de.kel0002.buildai.util.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GenerateTabCompletion implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player player = (Player) commandSender;

        ArrayList<String> completers = new ArrayList<>();
        if (args.length == 1){
            ArrayList<String> models = ConfigManager.get_model_list();
            completers.addAll(models);
        }

        if (args.length >= 2 && args.length <= 7) {
            boolean allIntegers = true;

            for (int i = 1; i < args.length-1; i++) {
                if (!isInteger(args[i])) {
                    allIntegers = false;
                    break;
                }
            }

            if (allIntegers) {
                Block targetBlock = player.getTargetBlock(null, 100);
                if (targetBlock != null) {
                    Location blockLocation = targetBlock.getLocation();
                    int coordinate = 0;

                    if (args.length == 2 || args.length == 5) {
                        coordinate = blockLocation.getBlockX();
                        completers.add(blockLocation.getBlockX() + " " + blockLocation.getBlockY() + " " + blockLocation.getBlockZ());
                    } else if (args.length == 3 || args.length == 6) {
                        coordinate = blockLocation.getBlockY();
                        completers.add(blockLocation.getBlockY() + " " + blockLocation.getBlockZ());
                    } else if (args.length == 4 || args.length == 7) {
                        coordinate = blockLocation.getBlockZ();
                    }

                    completers.add(String.valueOf(coordinate));
                }
            }
        }
        return completers;
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
