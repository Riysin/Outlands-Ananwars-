package me.orange.anan.blocks;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.nbt.NBTKey;
import io.fairyproject.bukkit.nbt.NBTModifier;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.config.BuildConfig;
import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.craft.CraftType;
import me.orange.anan.blocks.config.NatureBlockConfig;
import me.orange.anan.blocks.config.NatureBlockElement;
import me.orange.anan.craft.behaviour.teamCore.TeamCore;
import me.orange.anan.craft.behaviour.teamCore.TeamCoreManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

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
        Block block = event.getBlock();
        BlockStats blockStats = blockStatsManager.getBlockStats(block);

        //Managing the health of the block
        if (blockStats.getBlockType() == BlockType.BUILDING) {
            blockStatsManager.breakBlock(player, block);
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

        if (craft == null || craft.getType() != CraftType.BUILD && craft.getType() != CraftType.USAGE) {
            if (player.getGameMode() != GameMode.CREATIVE)
                event.setCancelled(true);
            return;
        } else if (isBesideNatureBlock(block)) {
            event.setCancelled(true);
            player.sendMessage("§c你不能在可挖掘的資源旁建造方塊!");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 0.2f);
            return;
        }

        int health = buildConfig.getBuildBlocks().get(nbtValue);
        blockStatsManager.placeBlock(event.getPlayer(), event.getBlockPlaced(), health);

        BlockStats blockStats = blockStatsManager.getBlockStats(block);
        if (blockStats != null && blockStats.getBlockType() == BlockType.BUILDING) {
            Bukkit.broadcastMessage("Block is a building block: " + block.getType());
            TeamCore teamCore = teamCoreManager.getTeamCoreByLocation(block.getLocation());
            if (teamCore != null) {
                Bukkit.broadcastMessage("Block is in team territory: " + block.getType());
                teamCoreManager.addConnectedTeamBlocks(teamCore, block);
            }
        }
    }

    private boolean isBesideNatureBlock(Block block) {
        return isNatureBlock(block.getLocation().clone().add(0, 1, 0).getBlock())
                || isNatureBlock(block.getLocation().clone().add(0, -1, 0).getBlock())
                || isNatureBlock(block.getLocation().clone().add(1, 0, 0).getBlock())
                || isNatureBlock(block.getLocation().clone().add(-1, 0, 0).getBlock())
                || isNatureBlock(block.getLocation().clone().add(0, 0, 1).getBlock())
                || isNatureBlock(block.getLocation().clone().add(0, 0, -1).getBlock());
    }

    private boolean isNatureBlock(Block block) {
        Integer id = block.getTypeId();

        for (NatureBlockElement natureBlock : natureBlockConfig.getNatureBlocks()) {
            Integer data = natureBlock.getData();
            if (id.equals(natureBlock.getBlockId()) && (data == -1 || block.getData() == data))
                return true;
        }
        return false;
    }

    @EventHandler
    public void onTargetBlock(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        //ignored materials
        Set<Material> materials = new HashSet<>();
        materials.add(XMaterial.AIR.parseMaterial());
        materials.add(XMaterial.WATER.parseMaterial());
        materials.add(XMaterial.LAVA.parseMaterial());

        Block targetBlock = player.getTargetBlock(materials, 10);

        if (targetBlock != null) {
            BlockStats blockStats = blockStatsManager.getBlockStats(targetBlock);
            if (blockStats != null && blockStats.getBlockType() == BlockType.BUILDING) {
                String blockName = targetBlock.getType().name();
                ActionBar.sendActionBar(player, " health:§a " + blockStats.getHealth());
            }
        }
    }

}
