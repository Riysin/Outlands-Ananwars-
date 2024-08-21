package me.orange.anan.blocks;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import me.orange.anan.blocks.config.BlockConfig;
import me.orange.anan.blocks.config.BlockConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@InjectableComponent
public class BlockStatsManager {
    private final BlockConfig blockConfig;
    private Map<Block, BlockStats> blockStatsMap = new HashMap<>();

    public BlockStatsManager(BlockConfig blockConfig) {
        this.blockConfig = blockConfig;

        loadConfig();
    }

    public void loadConfig() {
        for (BlockConfigElement element : blockConfig.getBlockData()) {
            Block block = element.getLocation().getBlock();
            BlockStats blockStats = getBlockStats(block);
            blockStats.setBlockType(element.getBlockType());
            blockStats.setHealth(element.getHealth());
            blockStats.setLocation(element.getLocation());
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

    public void breakBlock(Player player, Block block) {
        BlockStats blockStats = getBlockStats(block);
        blockStats.setHealth(blockStats.getHealth() - 1);

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
            blockStats.setGettingDestroyed(false);
        }, Duration.ofSeconds(60));
    }
}
