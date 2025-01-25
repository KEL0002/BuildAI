package de.kel0002.buildai.Selection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Selection {
    static HashMap<Player, Location> pos1map = new HashMap<>();
    static HashMap<Player, Location> pos2map = new HashMap<>();
    static HashMap<Player, FancySelectionBox> boxmap = new HashMap<>();


    public static void setPos1(Player player, Location location){
        pos1map.put(player, location);
        particle_manager(player);
    }
    public static void setPos2(Player player, Location location){
        pos2map.put(player, location);
        particle_manager(player);
    }

    public static Location getPos1(Player player){
        return pos1map.get(player);
    }
    public static Location getPos2(Player player){
        return pos2map.get(player);
    }

    public static void particle_manager(Player player){
        if (getPos1(player) == null || getPos2(player) == null){
            return;
        }
        if (boxmap.get(player) != null && !boxmap.get(player).is_in_generation_mode) boxmap.get(player).stop();
        FancySelectionBox boxclass = new FancySelectionBox(player);
        boxclass.start_drawing_box(false);
        boxmap.put(player, boxclass);
    }

    public static FancySelectionBox get_boxclass(Player player){
        return boxmap.get(player);
    }
}
