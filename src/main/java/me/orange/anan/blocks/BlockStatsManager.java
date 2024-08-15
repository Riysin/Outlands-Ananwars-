package me.orange.anan.blocks;

import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.config.BlockConfig;
import me.orange.anan.blocks.config.BlockConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.*;

@InjectableComponent
public class BlockStatsManager {
    private BlockConfig blockConfig;
    private Map<Block, BlockStats> blockStatsMap = new HashMap<>();

    public BlockStatsManager(BlockConfig blockConfig) {
        this.blockConfig = blockConfig;

        loadBlockStats();
    }
    public void loadBlockStats() {
        for (BlockConfigElement element : blockConfig.getBlockData()) {
            Block block = element.getLocation().getBlock();
            BlockStats blockStats = getBlockStats(block);
            blockStats.setBlockType(element.getBlockType());
            blockStats.setHealth(element.getHealth());
        }
    }

    public BlockStats getBlockStats(Block block) {
        return blockStatsMap.computeIfAbsent(block, k -> new BlockStats());
    }

    public void updateBlockStats(Block block) {
        BlockStats blockStats = getBlockStats(block);
        BlockConfigElement element = blockConfig.getBlockConfigElement(block);

        blockStats.setBlockType(element.getBlockType());
        blockStats.setHealth(element.getHealth());
    }
    public Map<Block, BlockStats> getBlockStatsMap() {
        return blockStatsMap;
    }

    public void setBlockStatsMap(Map<Block, BlockStats> blockStatsMap) {
        this.blockStatsMap = blockStatsMap;
    }

    public void breakBlock(Player player, Block block) {
        BlockConfigElement element = blockConfig.getBlockConfigElement(block);
        element.setHealth(element.getHealth()-1);
        updateBlockStats(block);

        Bukkit.getPluginManager().callEvent(new PlayerMoveEvent(player, player.getLocation(), player.getLocation()));
    }

    public boolean checkBlockBreak(Block block) {
        BlockStats blockStats = getBlockStats(block);
        return blockStats.getHealth() <= 0;
    }

    public BlockStats placeBlock(Player player, Block block, Integer health) {
        blockConfig.addBlock(block, health);
        updateBlockStats(block);
        return getBlockStats(block);
    }

    public BlockConfigElement getBlockConfigElement(Block block) {
        return blockConfig.getBlockConfigElement(block);
    }

}
