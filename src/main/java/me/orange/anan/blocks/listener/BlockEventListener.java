package me.orange.anan.blocks.listener;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.nbt.NBTKey;
import io.fairyproject.bukkit.nbt.NBTModifier;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStats;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.blocks.BlockType;
import me.orange.anan.blocks.config.BuildConfig;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.craft.CraftType;
import me.orange.anan.blocks.config.NatureBlockConfig;
import me.orange.anan.blocks.config.NatureBlockElement;
import me.orange.anan.craft.behaviour.lock.LockManager;
import me.orange.anan.craft.behaviour.teamCore.TeamCore;
import me.orange.anan.craft.behaviour.teamCore.TeamCoreManager;
import me.orange.anan.events.BlockResourceBreakEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Door;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@InjectableComponent
@RegisterAsListener
public class BlockEventListener implements Listener {
    private final BlockStatsManager blockStatsManager;
    private final CraftManager craftManager;
    private final NatureBlockConfig natureBlockConfig;
    private final BuildConfig buildConfig;
    private final TeamCoreManager teamCoreManager;

    public BlockEventListener(BlockStatsManager blockStatsManager, CraftManager craftManager, NatureBlockConfig natureBlockConfig, BuildConfig buildConfig, TeamCoreManager teamCoreManager) {
        this.blockStatsManager = blockStatsManager;
        this.craftManager = craftManager;
        this.natureBlockConfig = natureBlockConfig;
        this.buildConfig = buildConfig;
        this.teamCoreManager = teamCoreManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = blockStatsManager.getMainBlock(event.getBlock());
        BlockStats blockStats = blockStatsManager.getBlockStats(block);

        //Managing the health of the block
        if (blockStats.getBlockType() == BlockType.BUILDING) {
            blockStatsManager.breakBlock(player, block, player.getItemInHand());
            if (player.getGameMode() == GameMode.CREATIVE)
                blockStats.setHealth(0);

            if (blockStats.getHealth() <= 0) {
                blockStatsManager.getBlockStatsMap().remove(block);
            } else {
                event.setCancelled(true);
            }
            return;
        }

        //Dropping items
        Integer id = block.getTypeId();
        byte data = block.getData();
        boolean drop = false;

        for (NatureBlockElement natureBlock : natureBlockConfig.getNatureBlocks()) {
            Integer natureBlockData = natureBlock.getData();
            Integer natureBlockBlockId = natureBlock.getBlockId();

            if (id.equals(natureBlockBlockId) && (natureBlockData == -1 || data == natureBlockData)) {
                Bukkit.getPluginManager().callEvent(new BlockResourceBreakEvent(player, block));

                if (natureBlock.getDrops().isEmpty()) {
                    drop = true;
                    break;
                }

                for (String key : natureBlock.getDrops().keySet()) {
                    Craft craft = craftManager.getCrafts().get(key);
                    ItemStack itemStack = craftManager.getItemStack(craft, player);
                    itemStack.setAmount(natureBlock.getDrops().get(key));
                    dropItem(player, itemStack);
                    drop = true;
                }
                break;
            }
        }

        if (!drop && player.getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    private void dropItem(Player player, ItemStack itemStack) {
        player.getInventory().addItem(itemStack).forEach((k, v) -> {
            player.getWorld().dropItem(player.getLocation(), v);
        });
        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        Block block = event.getBlock();
        String nbtValue = NBTModifier.get().getString(item, NBTKey.create("craft"));
        Craft craft = craftManager.getCrafts().get(nbtValue);

        if (craft == null || (craft.getType() != CraftType.BUILD && craft.getType() != CraftType.USAGE)) {
            if (player.getGameMode() != GameMode.CREATIVE)
                event.setCancelled(true);
            return;
        }

        if (blockStatsManager.isBesideNatureBlock(block)) {
            event.setCancelled(true);
            player.sendMessage("§c你不能在可挖掘的資源旁建造方塊!");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 0.2f);
            return;
        }

        if (teamCoreManager.isAboveOtherTeamBlock(player, block) && block.getType() != Material.LADDER) {
            event.setCancelled(true);
            player.sendMessage("§c你不能在他人領地的上方建造方塊!");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 0.2f);
            return;
        }

        if (teamCoreManager.otherTeamBlockInRadius(player, block) && block.getType() != Material.LADDER) {
            event.setCancelled(true);
            player.sendMessage("§c你不能在他人領地的旁建造方塊!");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 0.2f);
            return;
        }

        int health = buildConfig.getBuildBlocks().get(nbtValue);
        blockStatsManager.placeBlock(event.getPlayer(), event.getBlockPlaced(), health);

        //Check if the block is connected to a team core
        TeamCore adjacentTeamCore = teamCoreManager.findAdjacentBlockTeamCore(block);
        BlockStats blockStats = blockStatsManager.getBlockStats(block);
        if (blockStats != null && blockStats.getBlockType() == BlockType.BUILDING) {
            if (adjacentTeamCore != null) {
                teamCoreManager.addConnectedTeamBlocks(adjacentTeamCore, block);
            }
        }
    }
}
