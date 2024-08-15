package me.orange.anan.craft.behaviour.teamCore;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStats;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.blocks.BlockType;
import me.orange.anan.events.PlayerLeftClickHammerEvent;
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

        BlockStats belowBlockStat = blockStatsManager.getBlockStats(world.getBlockAt(location.clone().add(0, -1, 0)));

        if (belowBlockStat == null || belowBlockStat.getBlockType() != BlockType.BUILDING) {
            sendErrorMessage(player);
            event.setCancelled(true);
            return;
        }

        Creeper teamCore = spawnTeamCore(location, world);
        teamCoreManager.addTeamCore(player, block, teamCore);
    }

    private void sendErrorMessage(Player player) {
        player.sendMessage("§c隊伍核心只能放置在建築方塊上方!");
        player.playSound(player.getLocation(), ERROR_SOUND, SOUND_VOLUME, SOUND_PITCH);
    }

    private Creeper spawnTeamCore(Location location, World world) {
        Location spawnLocation = location.clone().add(CREEPER_SPAWN_XZ_OFFSET, CREEPER_SPAWN_Y_OFFSET, CREEPER_SPAWN_XZ_OFFSET);
        Creeper teamCore = world.spawn(spawnLocation, Creeper.class);

        teamCore.setCustomNameVisible(false);
        teamCore.setRemoveWhenFarAway(false);
        teamCore.setMaxHealth(CORE_HEALTH);
        teamCore.setHealth(CORE_HEALTH);

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "entitydata " + teamCore.getUniqueId() + " {NoAI:1b}");
        return teamCore;
    }

    @EventHandler
    public void onTeamCoreDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Creeper) {
            Creeper creeper = (Creeper) entity;

            TeamCore teamCore = teamCoreManager.getTeamCore(creeper);
            Creeper coreCreeper = teamCore.getCoreCreeper();
            if (coreCreeper == null) return;

            Block placedBlock = teamCore.getCoreBlock();
            if (placedBlock != null) {
                placedBlock.setType(XMaterial.AIR.parseMaterial());
            }

            // Remove the team core from the map and the manager
            teamCoreManager.removeTeamCore(teamCore);
            teamCoreManager.getTeamCores().remove(teamCore);
        }
    }

    @EventHandler
    public void onLookAtTeamCore(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Creeper sightCreeper = getSightCreeper(player);
        if (sightCreeper != null) {
            ActionBar.sendActionBar(player, "health: §a" + sightCreeper.getHealth());
        }

    }

    private Creeper getSightCreeper(Player player) {
        for (TeamCore teamCore : teamCoreManager.getTeamCores()) {
            if (player.hasLineOfSight(teamCore.getCoreCreeper())) {
                return teamCore.getCoreCreeper();
            }
        }
        return null;
    }
}
