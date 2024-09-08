package me.orange.anan.craft.behaviour.teamCore;

import io.fairyproject.bukkit.events.player.EntityDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStats;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.blocks.BlockType;
import me.orange.anan.events.PlayerPlaceTeamCoreEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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

        // Pre-clone once and reuse.
        Location belowLocation = location.clone().add(0, -1, 0);
        BlockStats belowBlockStat = blockStatsManager.getBlockStats(world.getBlockAt(belowLocation));

        if (belowBlockStat == null || belowBlockStat.getBlockType() != BlockType.BUILDING) {
            sendErrorMessage(player);
            event.setCancelled(true);
            return;
        }

        if(teamCoreManager.isNearTeamCore(block) && block.getType() == Material.ENDER_PORTAL_FRAME) {
            event.setCancelled(true);
            player.sendMessage("§c你不能在隊伍核心附近放置其他隊伍核心!");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 0.2f);
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
        Block block = event.getBlock();

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
    public void onRightClickCreeper(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Creeper) {
            Creeper creeper = (Creeper) event.getRightClicked();
            TeamCore teamCore = teamCoreManager.getTeamCore(creeper);
            if (teamCore != null)
                if (player.isSneaking())
                    player.openInventory(teamCore.getInventory());
        }
    }

    @EventHandler
    public void onCreeperHurt(EntityDamageByPlayerEvent event) {
        if (event.getEntity() instanceof Creeper) {
            Bukkit.getPluginManager().callEvent(new PlayerMoveEvent(event.getDamager(), event.getDamager().getLocation(), event.getDamager().getLocation()));
        }
    }
}
