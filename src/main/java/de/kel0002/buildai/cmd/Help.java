package de.kel0002.buildai.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Help implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {


        sendMessage(sender, "§6§lBuildAI Command Usage:");
        sendMessage(sender, "§e/aigen MODEL_PRESET X1 Y1 Z1 X2 Y2 Z2 SOMEVAR=SOMEVALUE PROMPT");
        sendMessage(sender, "§7- §f§lmodel_preset:§r A configuration for specific AI models, defined in the config. Use the Tab-Completer to view available presets.");
        sendMessage(sender, "§7- §f§lx1 y1 z1 x2 y2 z2:§r The coordinates defining the area to fill. If you have a WorldEdit selection, this step is optional.");
        sendMessage(sender, "§7- §f§lsomevar=somevalue:§r Set a variable to a specific value. Variables are defined by the server owner in the config. This can be put anywhere after the coordinates.");
        sendMessage(sender, "§7- §f§lprompt:§r The input text that the AI will use to generate the structure.");
        sendMessage(sender, " ");
        sendMessage(sender, "§6Additional Notes:");
        sendMessage(sender, "§7- §fAPI Keys: If the server requests an API key, be cautious. API keys will be visible in the server console.");
        sendMessage(sender, "§7- §fThe prompt will be formatted according to the configuration before being sent to the AI model.");





        return false;
    }


    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage("§f" + message);
    }
}
