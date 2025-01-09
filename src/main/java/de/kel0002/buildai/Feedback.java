package de.kel0002.buildai;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Feedback {
    public static void sendFeedback(CommandSender player, String name, String parameter){
        player.sendMessage("§8[§3Build§5AI§8] " + getfeedbackMSG(name).replace("{param}", parameter));
    }
    public static void sendFeedback(CommandSender player, String name){
        sendFeedback(player, name, "");
    }

    public static void sendActionBarFeedback(CommandSender player, String name, String parameter){
        if (player instanceof Player){
            String rawmsg = "§8[§3Build§5AI§8] " + getfeedbackMSG(name).replace("{param}", parameter);
            TextComponent component = Component.text(rawmsg);
            player.sendActionBar(component);
        }
    }

    public static void sendActionBarFeedback(CommandSender player, String name){
        sendActionBarFeedback(player, name, "");
    }

    public static void clearActionBar(Player player){
            TextComponent component = Component.text("");
            player.sendActionBar(component);
    }


    public static String getfeedbackMSG(String name){
        switch (name){
            case "error.not_a_player": return "§4You have to be a player to perform this command";
            case "error.selection": return "§4Please provide the coordinates in the area you want to be build or make a selection using WorldEdit";
            case "error.usage": return "§4Usage: /generate x1 y1 z1 x2 y2 z2 model_preset prompt or /generate model_preset prompt with a WorldEdit selection";

            case "error.config.prompt": return "§4Could not get the prompt from the config.yml file. Check for incorrect formatting or try deleting it";
            case "error.unset_vars": return "§4The following variable(s) are not set: {param}. Set them by including SomeVariable=SomeValue in your input";

            case "error.request": return "§4Did not get a response from the server. Confirm that the endpoint is online and check url in the config.yml file";
            case "error.request.extract": return "§4The plugin could not get the content of the server's response. The server returned: {param}";
            case "error.model_preset_payload": return "§4There was an error getting the payload from your desired model";

            case "error.setblock": return "§cError executing the setblock command provided by the AI: /{param}";
            case "error.fill": return "§cError executing the fill command provided by the AI: /{param}";


            case "info.structure": return "§7You tasked the AI to build '{param}'";
            case "info.payload": return "§fPayload: §7{param}";
            case "info.response.header": return "§fResponse from AI:";
            case "info.response.content.correct": return "    §f{param}";
            case "info.response.content.incorrect": return "    §7{param}";

            case "wait.generating.actionbar": return "§eWaiting for response{param}";
            case "wait.placing.actionbar": return "§ePlacing blocks{param}";

            case "wait.generating": return "§eWaiting for Response...";
            case "wait.execution": return "§eResponse gotten, performing provided commands";

            case "done.done": return "§aAI is done building";
        }
        return "§4§l" + name;
    }
}
