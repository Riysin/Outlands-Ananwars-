package me.orange.anan.blocks;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.config.BlockConfig;
import me.orange.anan.blocks.config.BlockConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

@InjectableComponent
public class BlockStatsManager {
    private final BlockConfig blockConfig;
    private Map<Block, BlockStats> blockStatsMap = new HashMap<>();

    public BlockStatsManager(BlockConfig blockConfig) {
        this.blockConfig = blockConfig;

        loadConfig();
    }

    public void loadConfig() {
        blockStatsMap.clear();
        for (BlockConfigElement element : blockConfig.getBlockData()) {
            Block block = element.getLocation().getBlock();
            BlockStats blockStats = getBlockStats(block);
            blockStats.setBlockType(element.getBlockType());
            blockStats.setHealth(element.getHealth());
            blockStats.setLocation(element.getLocation());
        }
    }

    public void saveConfig() {
        blockConfig.getBlockData().clear();
        blockStatsMap.forEach((block, blockStats) -> {
            BlockConfigElement element = new BlockConfigElement();
            element.setBlockType(blockStats.getBlockType());
            element.setHealth(blockStats.getHealth());
            element.setPosition(block.getLocation());
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
        return blockStatsMap.computeIfAbsent(block, k -> new BlockStats());
    }

    public void breakBlock(Player player, Block block) {
        BlockStats blockStats = getBlockStats(block);
        blockStats.setHealth(blockStats.getHealth() - 1);

        Bukkit.getPluginManager().callEvent(new PlayerMoveEvent(player, player.getLocation(), player.getLocation()));
    }

    public BlockStats placeBlock(Player player, Block block, Integer health) {
        BlockStats blockStats = getBlockStats(block);
        blockStats.setHealth(health);
        blockStats.setBlockType(BlockType.BUILDING);
        return getBlockStats(block);
    }
}
