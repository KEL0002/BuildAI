package de.kel0002.buildai.Selection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Selection {
    static HashMap<Player, Location> pos1map = new HashMap<>();
    static HashMap<Player, Location> pos2map = new HashMap<>();


    public static void setPos1(Player player, Location location){
        pos1map.put(player, location);
    }
    public static void setPos2(Player player, Location location){
        pos2map.put(player, location);
    }

    public static Location getPos1(Player player){
        return pos1map.get(player);
    }
    public static Location getPos2(Player player){
        return pos2map.get(player);
    }

}
