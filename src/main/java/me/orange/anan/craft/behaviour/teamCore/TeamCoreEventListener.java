package me.orange.anan.craft.behaviour.teamCore;

import io.fairyproject.bukkit.events.player.EntityDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStats;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.blocks.BlockType;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.craft.CraftType;
import me.orange.anan.craft.behaviour.lock.LockManager;
import me.orange.anan.events.PlayerPlaceTeamCoreEvent;
import me.orange.anan.events.TeamCoreLockEvent;
import me.orange.anan.events.TeamCoreUnlockEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
    private final CraftManager craftManager;
    private final LockManager lockManager;
    private final ClanManager clanManager;

    public TeamCoreEventListener(TeamCoreManager teamCoreManager, BlockStatsManager blockStatsManager, CraftManager craftManager, LockManager lockManager, ClanManager clanManager) {
        this.teamCoreManager = teamCoreManager;
        this.blockStatsManager = blockStatsManager;
        this.craftManager = craftManager;
        this.lockManager = lockManager;
        this.clanManager = clanManager;
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

        if (teamCoreManager.isNearTeamCore(block) && block.getType() == Material.ENDER_PORTAL_FRAME) {
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
        if (teamCoreManager.isCoreCreeper(event.getEntity())) {
            event.getDrops().clear();
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

        if (!teamCoreManager.isCoreCreeper(event.getRightClicked())) return;

        TeamCore teamCore = teamCoreManager.getTeamCore((Creeper) event.getRightClicked());

        if (player.getItemInHand().isSimilar(craftManager.getItemStack("lock", player))) {
            if (!lockManager.hasLock(teamCore))
                Bukkit.getPluginManager().callEvent(new TeamCoreLockEvent(player, teamCoreManager.getTeamCore(player)));
            else
                player.sendMessage("§c此隊伍核心已經被上鎖!");

            return;
        }
        if (player.getItemInHand().isSimilar(craftManager.getItemStack("key", player))) {
            if (lockManager.hasLock(teamCore))
                Bukkit.getPluginManager().callEvent(new TeamCoreUnlockEvent(player, teamCoreManager.getTeamCore(player)));
            else
                player.sendMessage("§c此隊伍核心沒有被上鎖!");
            return;
        }

        if (player.isSneaking()) {
            if (lockManager.hasLock(teamCore) && !clanManager.sameClan(player, lockManager.getLock(teamCore).getOfflineOwner())) {
                player.sendMessage("§c此隊伍核心已經被上鎖!");
                return;
            }
            player.openInventory(teamCore.getInventory());
        }
    }


    @EventHandler
    public void onTeamInventoryClicked(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        if (teamCoreManager.getTeamCore(player) != null && teamCoreManager.getTeamCore(player).getInventory().equals(inventory)) {
            ItemStack itemStack = event.getCurrentItem();
            if (itemStack != null && craftManager.getCraft(itemStack) != null && craftManager.getCraft(itemStack).getType() != CraftType.RESOURCE) {
                player.sendMessage("§c你不能將非資源物品放入隊伍核心!");
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onCreeperHurt(EntityDamageByPlayerEvent event) {
        if (teamCoreManager.isCoreCreeper(event.getEntity())) {
            Bukkit.getPluginManager().callEvent(new PlayerMoveEvent(event.getDamager(), event.getDamager().getLocation(), event.getDamager().getLocation()));
        }
    }
}

