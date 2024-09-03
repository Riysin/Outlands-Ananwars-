package me.orange.anan.blocks;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import me.orange.anan.events.BlockResourceBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@InjectableComponent
@RegisterAsListener
public class ResourceEventListener implements Listener {
    private Set<Location> treeLogs = new HashSet<>();
    private final Random random = new Random();

    @EventHandler
    public void onResourceBreak(BlockResourceBreakEvent event) {
        Block block = event.getBlock();

        if (block.getType() == Material.LOG) { // You can expand this to include other tree types
            if (treeLogs.isEmpty()) {
                // Start tracking a new tree
                trackTree(block);
            }

            // Remove the block from the set of logs
            treeLogs.remove(block.getLocation());

            // If all logs are chopped, respawn the tree
            if (treeLogs.isEmpty()) {
                final Location treeLocation = block.getLocation();
                Bukkit.broadcastMessage("Tree chopped down at " + treeLocation.toVector().toString());
                MCSchedulers.getGlobalScheduler().schedule(()->{
                    Location respawnLocation = getValidSpawnLocation(treeLocation, 10);
                    if (respawnLocation != null) {
                        respawnLocation.getWorld().generateTree(respawnLocation, TreeType.TREE);
                        Bukkit.broadcastMessage("Tree respawned at " + BukkitPos.toMCPos(respawnLocation));
                    }
                },20*10);
            }
        }
    }

    private void trackTree(Block baseBlock) {
        // Start from the base block and find all connected logs
        findAllConnectedLogs(baseBlock);
    }

    private void findAllConnectedLogs(Block block) {
        if (block.getType() == Material.LOG && !treeLogs.contains(block.getLocation())) {
            // Add the block to the set of logs
            treeLogs.add(block.getLocation());

            // Check all adjacent blocks (6 directions: up, down, north, south, east, west)
            findAllConnectedLogs(block.getRelative(0, 1, 0));  // Up
            findAllConnectedLogs(block.getRelative(0, -1, 0)); // Down
            findAllConnectedLogs(block.getRelative(1, 0, 0));  // East
            findAllConnectedLogs(block.getRelative(-1, 0, 0)); // West
            findAllConnectedLogs(block.getRelative(0, 0, 1));  // South
            findAllConnectedLogs(block.getRelative(0, 0, -1)); // North
        }
    }

    private Location getValidSpawnLocation(Location original, int radius) {
        Location spawnLocation;
        for (int i = 0; i < 10; i++) { // Try up to 10 times to find a valid location
            // Generate random offsets within the specified radius
            int xOffset = random.nextInt(radius * 2 + 1) - radius;
            int zOffset = random.nextInt(radius * 2 + 1) - radius;

            spawnLocation = original.clone().add(xOffset, 0, zOffset);
            spawnLocation.setY(spawnLocation.getWorld().getHighestBlockYAt(spawnLocation));

            // Check if the block below is grass or dirt
            Block baseBlock = spawnLocation.getBlock().getRelative(0, -1, 0);
            if (baseBlock.getType() == Material.GRASS || baseBlock.getType() == Material.DIRT) {
                return spawnLocation; // Found a valid location
            }
        }
        return null; // No valid location found after 10 tries
    }
}
