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
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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

        BlockStats belowBlockStat = blockStatsManager.getBlockStats(world.getBlockAt(location.clone().add(0, -1, 0)));

        if (belowBlockStat == null || belowBlockStat.getBlockType() != BlockType.BUILDING) {
            sendErrorMessage(player);
            event.setCancelled(true);
            return;
        }

        Creeper coreCreeper = spawnTeamCore(location, world);
        teamCoreManager.addTeamCore(player, block, coreCreeper);

        TeamCore teamCore = teamCoreManager.getTeamCore(coreCreeper);
        addConnectedTeamBlocks(teamCore, block);
    }

    private void sendErrorMessage(Player player) {
        player.sendMessage("§c隊伍核心只能放置在建築方塊上方!");
        player.playSound(player.getLocation(), ERROR_SOUND, SOUND_VOLUME, SOUND_PITCH);
    }

    public Creeper spawnTeamCore(Location location, World world) {
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
            ActionBar.sendActionBar(player, "health: §a" + sightCreeper.getHealth());
        }

        Location location = player.getLocation();
        TeamCore teamCore = teamCoreManager.getTeamCoreByLocation(location);

        if (teamCore != null) {
            ActionBar.sendActionBar(player, "health: §a" + teamCore.getCoreCreeper().getHealth());
        }
    }

    private Creeper getSightCreeper(Player player) {
        return teamCoreManager.getTeamCores().stream()
                .map(TeamCore::getCoreCreeper)
                .filter(player::hasLineOfSight)
                .findFirst()
                .orElse(null);
    }

    public void addConnectedTeamBlocks(TeamCore teamCore, Block block) {
        Set<Block> visitedBlocks = new HashSet<>();
        exploreConnectedBlocks(teamCore, block, visitedBlocks);

        for (Block connectedBlock : teamCore.getConnectedBlocks()) {
            for (int i = 1; i <= 3; i++) {
                Block aboveBlock = connectedBlock.getRelative(0, i, 0);
                teamCore.addConnectedBlock(aboveBlock);
            }
        }
    }

    private void exploreConnectedBlocks(TeamCore teamCore, Block block, Set<Block> visitedBlocks) {
        if (visitedBlocks.contains(block)) return;

        visitedBlocks.add(block);
        teamCore.addConnectedBlock(block);

        BlockStats blockStats = blockStatsManager.getBlockStats(block);
        if (blockStats == null || blockStats.getBlockType() != BlockType.BUILDING) {
            return;
        }

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (Math.abs(dx) + Math.abs(dy) + Math.abs(dz) == 1) {
                        exploreConnectedBlocks(teamCore, block.getRelative(dx, dy, dz), visitedBlocks);
                    }
                }
            }
        }
    }

    private void removeConnectedBlocks(TeamCore teamCore) {
        for (Block block : new HashSet<>(teamCore.getConnectedBlocks())) {
            if (!isBlockConnected(teamCore, block)) {
                teamCore.getConnectedBlocks().remove(block);
                block.setType(XMaterial.AIR.parseMaterial());
            }
        }
    }

    private boolean isBlockConnected(TeamCore teamCore, Block block) {
        return teamCore.getConnectedBlocks().contains(block);
    }
}
