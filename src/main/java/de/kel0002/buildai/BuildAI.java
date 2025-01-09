package de.kel0002.buildai;

import de.kel0002.buildai.cmd.Generate;
import de.kel0002.buildai.cmd.GenerateTabCompletion;
import de.kel0002.buildai.cmd.Help;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuildAI extends JavaPlugin {
    private static BuildAI instance;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        this.getCommand("aigenerate").setExecutor(new Generate());
        this.getCommand("aigen").setExecutor(new Generate());

        this.getCommand("aigen").setTabCompleter(new GenerateTabCompletion());
        this.getCommand("aigenerate").setTabCompleter(new GenerateTabCompletion());

        this.getCommand("buildai").setExecutor(new Help());
    }
    public static BuildAI getInstance(){
            return instance;
    }

    @Override
    public void onDisable() {

    }


    public static boolean isWorldEditAvailable() {
    try {
        Class.forName("com.sk89q.worldedit.WorldEdit");
        return true;
    } catch (ClassNotFoundException e) {
        return false;
    }
}

}
