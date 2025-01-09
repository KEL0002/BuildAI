package de.kel0002.buildai.util;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import de.kel0002.buildai.BuildAI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;


public class WorldEditUtil {
    public static Location getPos1(Player player) {
        if (!BuildAI.isWorldEditAvailable()){
            return null;
        }
        try {
            LocalSession session = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player));
            if (session != null) {
                try {
                    BlockVector3 minPoint = session.getSelection(session.getSelectionWorld()).getMinimumPoint();
                    World bukkitWorld = player.getWorld();
                    return new Location(bukkitWorld, minPoint.getX(), minPoint.getY(), minPoint.getZ());
                } catch (Exception e) {
                    return null;
                }
            }

        } catch (Exception e){
            return null;
        }
        return null;
    }

    public static Location getPos2(Player player) {
        if (!BuildAI.isWorldEditAvailable()){
            return null;
        }
        try {
            LocalSession session = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player));
            if (session != null) {
                try {
                    BlockVector3 maxPoint = session.getSelection(session.getSelectionWorld()).getMaximumPoint();
                    World bukkitWorld = player.getWorld();
                    return new Location(bukkitWorld, maxPoint.getX(), maxPoint.getY(), maxPoint.getZ());

                } catch (Exception e) {
                    return null;
                }
            }
        } catch (Exception e){
            return null;
        }
        return null;
    }

}
