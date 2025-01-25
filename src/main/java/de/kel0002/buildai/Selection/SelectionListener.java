package de.kel0002.buildai.Selection;

import de.kel0002.buildai.Feedback;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SelectionListener implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent event){
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.WOODEN_SHOVEL) return;
        if (!event.getPlayer().isOp()) return;
        if (event.getClickedBlock() == null) return;

        if (event.getAction().isLeftClick()){
            Location blocklocation = event.getClickedBlock().getLocation();
            Selection.setPos1(event.getPlayer(), blocklocation);
            Feedback.sendFeedback(event.getPlayer(), "info.selection.pos1", (blocklocation.getBlockX() + " " + blocklocation.getBlockY() + " " + blocklocation.getBlockZ()));
        }
        else if (event.getAction().isRightClick()){
            Location blocklocation = event.getClickedBlock().getLocation();
            Selection.setPos2(event.getPlayer(), event.getClickedBlock().getLocation());
            Feedback.sendFeedback(event.getPlayer(), "info.selection.pos2", (blocklocation.getBlockX() + " " + blocklocation.getBlockY() + " " + blocklocation.getBlockZ()));
        }
        event.setCancelled(true);
    }
}

