package de.kel0002.buildai.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Help implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {


        sendMessage(sender, "§lBuildAIs command and usage:");
        sendMessage(sender, " /aigen model_preset x1 y1 z1 x2 y2 z2 somevar=somevalue prompt");
        sendMessage(sender, "The model_preset is a configuration for specific models that was given in the config");
        sendMessage(sender, " You can figure out what options you have by using the Tab-Completer");
        sendMessage(sender, "x1-z2 refers to the area you want to fill. You do not need to specify the area if you have a WorldEdit selection");
        sendMessage(sender, "You can set a variable to a value using somevar=somevalue anywhere after the area specification.");
        sendMessage(sender, " Variables were defined by the server owner in the config. ");
        sendMessage(sender, " §lIf the server asks you to set an api key, be careful: the api key will be visible in the server console");
        sendMessage(sender, "The prompt (input) is the input that will be edited according to the config and then send to the endpoint.");




        return false;
    }


    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage("§8[§3Build§5AI§8] §f" + message);
    }
}
