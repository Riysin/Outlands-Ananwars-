package me.orange.anan.craft.behaviour.teamCore;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStats;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.blocks.BlockType;
import me.orange.anan.events.PlayerPlaceTeamCoreEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

@InjectableComponent
@RegisterAsListener
public class TeamCoreEventListener implements Listener {
    private static final double CREEPER_SPAWN_Y_OFFSET = 0.8125;
    private static final double CREEPER_SPAWN_XZ_OFFSET = 0.5;
    private static final double CORE_HEALTH = 100.0;
    private static final Sound ERROR_SOUND = Sound.FIZZ;
    private static final float SOUND_VOLUME = 1.0f;
    private static final float SOUND_PITCH = 1.0f;

    private final TeamCoreManager teamCoreManager;
    private final BlockStatsManager blockStatsManager;

    public TeamCoreEventListener(TeamCoreManager teamCoreManager, BlockStatsManager blockStatsManager) {
        this.teamCoreManager = teamCoreManager;
        this.blockStatsManager = blockStatsManager;
    }

    @EventHandler
    public void onPlayerPlaceTeamCore(PlayerPlaceTeamCoreEvent event) {
        Player player = event.getPlayer();
        Block block = event.getPlaceBlock();
        Location location = block.getLocation();
        World world = player.getWorld();

        // Pre-clone once and reuse.
        Location belowLocation = location.clone().add(0, -1, 0);
        BlockStats belowBlockStat = blockStatsManager.getBlockStats(world.getBlockAt(belowLocation));

        if (belowBlockStat == null || belowBlockStat.getBlockType() != BlockType.BUILDING) {
            sendErrorMessage(player);
            event.setCancelled(true);
            return;
        }

        Creeper coreCreeper = spawnCoreCreeper(location, world);
        teamCoreManager.addTeamCore(player, coreCreeper, block);

        TeamCore teamCore = teamCoreManager.getTeamCore(coreCreeper);
        teamCoreManager.addConnectedTeamBlocks(teamCore, block);
    }

    private Creeper spawnCoreCreeper(Location location, World world) {
        Location spawnLocation = location.add(CREEPER_SPAWN_XZ_OFFSET, CREEPER_SPAWN_Y_OFFSET, CREEPER_SPAWN_XZ_OFFSET);
        Creeper creeper = world.spawn(spawnLocation, Creeper.class);

        creeper.setCustomNameVisible(false);
        creeper.setRemoveWhenFarAway(false);
        creeper.setMaxHealth(CORE_HEALTH);
        creeper.setHealth(CORE_HEALTH);

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "entitydata " + creeper.getUniqueId() + " {NoAI:1b}");
        return creeper;
    }

    private void sendErrorMessage(Player player) {
        player.sendMessage("§c隊伍核心只能放置在建築方塊上方!");
        player.playSound(player.getLocation(), ERROR_SOUND, SOUND_VOLUME, SOUND_PITCH);
    }

    @EventHandler
    public void onTeamCoreDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Creeper) {
            TeamCore teamCore = teamCoreManager.getTeamCore((Creeper) entity);
            if (teamCore != null) {
                Block placedBlock = teamCore.getCoreBlock();
                if (placedBlock != null) {
                    placedBlock.setType(XMaterial.AIR.parseMaterial());
                }

                removeConnectedBlocks(teamCore);
                teamCoreManager.removeTeamCore(teamCore);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Creeper sightCreeper = getSightCreeper(player);
        if (sightCreeper != null) {
            ActionBar.sendActionBar(player, "§6Core HP: §a" + sightCreeper.getHealth());
        }

        Location location = player.getLocation();
        TeamCore teamCore = teamCoreManager.getTeamCoreByLocation(location);

        if (teamCore != null) {
            ActionBar.sendActionBar(player, " §3You are in a territory");
        }
    }

    private Creeper getSightCreeper(Player player) {
        return teamCoreManager.getTeamCores().parallelStream()
                .map(TeamCore::getCoreCreeper)
                .filter(creeper -> player.hasLineOfSight(creeper) && isLookingAt(player, creeper))
                .findFirst()
                .orElse(null);
    }

    private boolean isLookingAt(Player player, Entity entity) {
        // Get the direction the player is facing
        Vector playerDirection = player.getLocation().getDirection();

        // Get the vector from the player to the entity
        Vector toEntity = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();

        // Calculate the angle between the player's direction and the entity
        double angle = playerDirection.angle(toEntity);

        // Return true if the angle is within 30 degrees (0.5236 radians)
        return angle < 0.5236; // approximately 30 degrees
    }


    private void removeConnectedBlocks(TeamCore teamCore) {
        Set<Block> blocksToRemove = new HashSet<>(teamCore.getConnectedBlocks());

        blocksToRemove.forEach(block -> {
            if (!isBlockConnected(teamCore, block)) {
                teamCore.getConnectedBlocks().remove(block);
                block.setType(XMaterial.AIR.parseMaterial());
            }
        });
    }

    private boolean isBlockConnected(TeamCore teamCore, Block block) {
        return teamCore.getConnectedBlocks().contains(block);
    }
}

