package me.orange.anan.blocks;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import me.orange.anan.blocks.config.BlockConfig;
import me.orange.anan.blocks.config.BlockConfigElement;
import me.orange.anan.blocks.config.NatureBlockConfig;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.craft.config.ToolConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;
import org.bukkit.material.Door;

import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

@InjectableComponent
public class BlockStatsManager {
    private final BlockConfig blockConfig;
    private final CraftManager craftManager;
    private final NatureBlockConfig natureBlockConfig;
    private Map<Block, BlockStats> blockStatsMap = new HashMap<>();

    public BlockStatsManager(BlockConfig blockConfig, CraftManager craftManager, NatureBlockConfig natureBlockConfig) {
        this.blockConfig = blockConfig;
        this.craftManager = craftManager;
        this.natureBlockConfig = natureBlockConfig;

        loadConfig();
    }

    public void loadConfig() {
        for (BlockConfigElement element : blockConfig.getBlockData()) {
            Block block = element.getLocation().getBlock();
            BlockStats blockStats = getBlockStats(block);
            blockStats.setBlockType(element.getBlockType());
            blockStats.setHealth(element.getHealth());
            blockStats.setLocation(element.getLocation());
            blockStats.setJustPlaced(false);
            blockStatsMap.put(block, blockStats);
        }
    }

    public void saveConfig() {
        blockConfig.getBlockData().clear();
        blockStatsMap.forEach((block, blockStats) -> {
            BlockConfigElement element = new BlockConfigElement();
            element.setBlockType(blockStats.getBlockType());
            element.setHealth(blockStats.getHealth());
            element.setLocation(block.getLocation());
            blockConfig.getBlockData().add(element);
        });
        blockConfig.save();
    }

    public Map<Block, BlockStats> getBlockStatsMap() {
        return blockStatsMap;
    }

    public void setBlockStatsMap(Map<Block, BlockStats> blockStatsMap) {
        this.blockStatsMap = blockStatsMap;
    }

    public BlockStats getBlockStats(Block block) {
        if (blockStatsMap.containsKey(block))
            return blockStatsMap.get(block);
        return new BlockStats();
    }

    public Block getMainBlock(Block block) {
        Material type = block.getType();
        if (type == Material.BED_BLOCK) {
            Bed bed = (Bed) block.getState().getData();
            if (bed.isHeadOfBed()) {
                return block.getRelative(bed.getFacing().getOppositeFace());
            }
        } else if (type == Material.WOODEN_DOOR || type == Material.IRON_DOOR_BLOCK || type == Material.IRON_DOOR) {
            Door door = (Door) block.getState().getData();
            if (door.isTopHalf()) {
                return block.getRelative(BlockFace.DOWN);
            }
        } else if (type == Material.CHEST || type == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) block.getState();
            Inventory inventory = chest.getInventory();

            // Check if it's a double chest
            if (inventory instanceof DoubleChestInventory) {
                DoubleChestInventory doubleChestInventory = (DoubleChestInventory) inventory;

                // Get the left side (main block) of the double chest
                Chest leftChest = (Chest) doubleChestInventory.getLeftSide().getHolder();

                if (leftChest != null) {
                    return leftChest.getBlock();
                }
            }
        }

        return block;
    }

    public void breakBlock(Player player, Block block, ItemStack toolItem) {
        BlockStats blockStats = getBlockStats(block);
        blockStats.setHealth(blockStats.getHealth() - craftManager.getDamage(toolItem));

        blockBreakScheduler(block);
        Bukkit.getPluginManager().callEvent(new PlayerMoveEvent(player, player.getLocation(), player.getLocation()));
    }

    public BlockStats placeBlock(Player player, Block block, Integer health) {
        BlockStats blockStats = getBlockStats(block);
        blockStats.setHealth(health);
        blockStats.setBlockType(BlockType.BUILDING);
        blockStats.setLocation(block.getLocation());
        blockStatsMap.put(block, blockStats);

        blockPlaceScheduler(block);
        return blockStats;
    }

    private void blockBreakScheduler(Block block) {
        BlockStats blockStats = getBlockStats(block);
        blockStats.setGettingDestroyed(true);

        MCSchedulers.getGlobalScheduler().schedule(() -> {
            blockStats.setGettingDestroyed(false);
        }, Duration.ofSeconds(30));
    }

    private void blockPlaceScheduler(Block block) {
        MCSchedulers.getGlobalScheduler().schedule(() -> {
            BlockStats blockStats = getBlockStats(block);
            blockStats.setJustPlaced(false);
        }, Duration.ofSeconds(60));
    }

    public boolean isBesideNatureBlock(Block block) {
        // Check surrounding blocks in six directions
        return Stream.of(
                block.getRelative(BlockFace.UP),
                block.getRelative(BlockFace.DOWN),
                block.getRelative(BlockFace.NORTH),
                block.getRelative(BlockFace.SOUTH),
                block.getRelative(BlockFace.EAST),
                block.getRelative(BlockFace.WEST)
        ).anyMatch(this::isNatureBlock);
    }

    private boolean isNatureBlock(Block block) {
        Integer id = block.getTypeId();
        byte data = block.getData();

        // Stream through nature blocks and check ID and data
        return natureBlockConfig.getNatureBlocks().stream().anyMatch(natureBlock ->
                id.equals(natureBlock.getBlockId()) && (natureBlock.getData() == -1 || data == natureBlock.getData())
        );
    }

    public boolean isBesideSameType(Block block, Material type) {
        return Stream.of(
                block.getRelative(BlockFace.UP),
                block.getRelative(BlockFace.DOWN),
                block.getRelative(BlockFace.NORTH),
                block.getRelative(BlockFace.SOUTH),
                block.getRelative(BlockFace.EAST),
                block.getRelative(BlockFace.WEST)
        ).anyMatch(b -> b.getType() == type);
    }
}
