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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

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
        teamCoreManager.addConnectedTeamBlocks(teamCore, world.getBlockAt(belowLocation));
    }

    private void sendErrorMessage(Player player) {
        player.sendMessage("§c隊伍核心只能放置在建築方塊上方!");
        player.playSound(player.getLocation(), ERROR_SOUND, SOUND_VOLUME, SOUND_PITCH);
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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        TeamCore teamCore = teamCoreManager.getTeamCore(block);
        if (teamCore != null) {
            teamCoreManager.removeTeamCore(teamCore);
            event.setCancelled(true);
            block.setType(XMaterial.AIR.parseMaterial());
            ActionBar.sendActionBar(player, "Team Core has been removed!");
        }

        teamCoreManager.onBlockBreak(block);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Creeper) {
            Creeper creeper = (Creeper) event.getEntity();
            TeamCore teamCore = teamCoreManager.getTeamCore(creeper);
            if (teamCore != null) {
                teamCoreManager.removeTeamCore(teamCore);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        Creeper sightCreeper = getSightCreeper(player);
        if (sightCreeper != null && location.distance(sightCreeper.getLocation()) < 4) {
            ActionBar.sendActionBar(player, "§6Core HP: §a" + sightCreeper.getHealth());
        }

        TeamCore teamCore = teamCoreManager.getTeamCoreByLocation(location);
        if (teamCore != null) {
            ActionBar.sendActionBar(player, "§3You are in a territory!");
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
        Vector playerDirection = player.getLocation().getDirection();
        Vector toEntity = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();

        double angle = playerDirection.angle(toEntity);
        return angle < 0.5236; // approximately 30 degrees
    }
}
