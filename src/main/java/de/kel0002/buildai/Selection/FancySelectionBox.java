package de.kel0002.buildai.Selection;

import de.kel0002.buildai.BuildAI;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;


public class FancySelectionBox {

    private BukkitTask particleTask;
    Location pos1;
    Location pos2;
    boolean is_in_generation_mode = false;

    public FancySelectionBox(Location pos1, Location pos2){
        this.pos1 = pos1;
        this.pos2 = pos2;

    }

    public void start_drawing_box(boolean generating) {
        int particles_per_block = BuildAI.getInstance().getConfig().getInt("particles_per_block");
        double step_amount = (double) 1/particles_per_block;

        Color color = Color.AQUA;

        if (generating){
            color = Color.PURPLE;
            is_in_generation_mode = true;
        }

        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1.0f);
        Particle particle = Particle.REDSTONE;

        double minX = Math.min(pos1.getX(), pos2.getX());
        double maxX = Math.max(pos1.getX(), pos2.getX()) + 1;
        double minY = Math.min(pos1.getY(), pos2.getY());
        double maxY = Math.max(pos1.getY(), pos2.getY()) + 1;
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ()) + 1;
        
        particleTask = Bukkit.getScheduler().runTaskTimer(BuildAI.getInstance(), () -> {
            if (particles_per_block == 0) return;
            for (double x = minX; x <= maxX; x += step_amount) {

                pos1.getWorld().spawnParticle(particle, x, minY, minZ, 0, dustOptions);
                pos1.getWorld().spawnParticle(particle, x, minY, maxZ, 0, dustOptions);

                pos1.getWorld().spawnParticle(particle, x, maxY, minZ, 0, dustOptions);
                pos1.getWorld().spawnParticle(particle, x, maxY, maxZ, 0, dustOptions);
            }

            for (double z = minZ; z <= maxZ; z += step_amount) {
                pos1.getWorld().spawnParticle(particle, minX, minY, z, 0, dustOptions);
                pos1.getWorld().spawnParticle(particle, maxX, minY, z, 0, dustOptions);

                pos1.getWorld().spawnParticle(particle, minX, maxY, z, 0, dustOptions);
                pos1.getWorld().spawnParticle(particle, maxX, maxY, z, 0, dustOptions);
            }

            for (double y = minY; y <= maxY; y += step_amount) {
                pos1.getWorld().spawnParticle(particle, minX, y, minZ, 0, dustOptions);
                pos1.getWorld().spawnParticle(particle, minX, y, maxZ, 0, dustOptions);

                pos1.getWorld().spawnParticle(particle, maxX, y, minZ, 0, dustOptions);
                pos1.getWorld().spawnParticle(particle, maxX, y, maxZ, 0, dustOptions);
            }
        }, 0L, 3L);
    }

    public void switch_to_generating(){
        stop();
        start_drawing_box(true);
    }


    public void stop(){
        if (particleTask != null) {
            particleTask.cancel();
            particleTask = null;
        }
    }
}
