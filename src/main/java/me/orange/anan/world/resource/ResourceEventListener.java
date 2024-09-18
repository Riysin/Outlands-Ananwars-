package me.orange.anan.world.resource;

import com.cryptomorin.xseries.XMaterial;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.util.TreeGenerator;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import me.orange.anan.craft.behaviour.teamCore.TeamCoreManager;
import me.orange.anan.events.BlockResourceBreakEvent;
import me.orange.anan.events.DayToNightEvent;
import me.orange.anan.events.NPCResourceDieEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@InjectableComponent
@RegisterAsListener
public class ResourceEventListener implements Listener {
    private final TeamCoreManager teamCoreManager;
    private final ResourceManager resourceManager;
    private final Set<Location> treeLogs = new HashSet<>();
    private final Random random = new Random();

    public ResourceEventListener(TeamCoreManager teamCoreManager, ResourceManager resourceManager) {
        this.teamCoreManager = teamCoreManager;
        this.resourceManager = resourceManager;
    }

    @EventHandler
    public void onResourceDie(NPCResourceDieEvent event) {
        NPC npc = event.getNpc();
        Player player = event.getPlayer();
        Block block = npc.getStoredLocation().getBlock();

        ((LivingEntity) npc.getEntity()).setHealth(0);
        npc.despawn();
        block.breakNaturally();
        player.playEffect(player.getLocation(), Effect.ZOMBIE_DESTROY_DOOR, 1);
        resourceManager.addResource(block);
    }

    @EventHandler
    public void onDayToNight(DayToNightEvent event) {
        World world = event.getWorld();
        Bukkit.broadcastMessage("It's night time! Respawning resources...");
        resourceManager.respawnOre(world);
        resourceManager.respawnLoot(world);
    }

    @EventHandler
    public void onResourceBreak(BlockResourceBreakEvent event) {
        Block block = event.getBlock();
        resourceManager.addResource(block);

        if (isLog(block.getType())) {
            if (treeLogs.isEmpty()) {
                trackTree(block);
            }
            treeLogs.remove(block.getLocation());

            if (treeLogs.isEmpty()) {
                scheduleTreeRespawn(block.getLocation(), getTreeType(block));
            }
        }
    }

    private boolean isLog(Material material) {
        return material == Material.LOG || material == Material.LOG_2;
    }

    private void trackTree(Block baseBlock) {
        // Start from the base block and find all connected logs
        findAllConnectedLogs(baseBlock);
    }

    private void findAllConnectedLogs(Block block) {
        if (isLog(block.getType()) && !treeLogs.contains(block.getLocation())) {
            treeLogs.add(block.getLocation());

            // Recursively find all connected logs in 6 directions
            for (BlockFace face : BlockFace.values()) {
                findAllConnectedLogs(block.getRelative(face));
            }
        }
    }

    private void scheduleTreeRespawn(Location treeLocation, TreeGenerator.TreeType treeType) {
        MCSchedulers.getGlobalScheduler().schedule(() -> {
            Location respawnLocation = getValidSpawnLocation(treeLocation, 10);
            if (respawnLocation != null) {
                respawnTreeAtLocation(respawnLocation, treeType);
            }
        }, 20 * 10);
    }

    private void respawnTreeAtLocation(Location location, TreeGenerator.TreeType treeType) {
        location.getBlock().setType(Material.AIR);
        Vector vector = new Vector(location.getX(), location.getY(), location.getZ());
        try {
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitUtil.getLocalWorld(location.getWorld()), 10000);
            treeType.generate(editSession, vector);
            Bukkit.broadcastMessage("Tree respawned at " + BukkitPos.toMCPos(location));
        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }
    }

    private Location getValidSpawnLocation(Location original, int radius) {
        for (int i = 0; i < 100; i++) { // Try up to 100 times to find a valid location
            Location spawnLocation = original.clone().add(randomOffset(radius), 0, randomOffset(radius));
            spawnLocation.setY(spawnLocation.getWorld().getHighestBlockYAt(spawnLocation));

            Block baseBlock = spawnLocation.getBlock().getRelative(0, -1, 0);
            if (isValidSpawnLocation(baseBlock)) {
                return spawnLocation;
            }
        }
        return null;
    }

    private int randomOffset(int radius) {
        return random.nextInt(radius * 2 + 1) - radius;
    }

    private boolean isValidSpawnLocation(Block block) {
        return (block.getType() == Material.GRASS || block.getType() == Material.DIRT || block.getType() == Material.SNOW)
                && !teamCoreManager.isInTerritory(block.getLocation());
    }

    private TreeGenerator.TreeType getTreeType(Block block) {
        Material material = block.getType();
        int data = block.getData();
        if (material == Material.LOG) {
            switch (data) {
                case 0:
                    return TreeGenerator.TreeType.TREE;
                case 1:
                    return TreeGenerator.TreeType.REDWOOD;
                case 2:
                    return TreeGenerator.TreeType.BIRCH;
                case 3:
                    return TreeGenerator.TreeType.JUNGLE;
            }
        } else if (material == Material.LOG_2) {
            return data == 0 ? TreeGenerator.TreeType.ACACIA : TreeGenerator.TreeType.DARK_OAK;
        }
        return null;
    }
}
