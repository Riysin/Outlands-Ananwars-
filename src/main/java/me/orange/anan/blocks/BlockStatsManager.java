package me.orange.anan.blocks;

import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class BlockStatsManager {
    private Map<Block, BlockStats> blockStatsMap = new HashMap<>();

    public BlockStats getBlockStats(Block block) {
        if (!getBlockStatsMap().containsKey(block)) {
            BlockStats bs = new BlockStats(10);
            getBlockStatsMap().put(block, bs);
        }
        return blockStatsMap.get(block);
    }

    public Map<Block, BlockStats> getBlockStatsMap() {
        return blockStatsMap;
    }

    public void setBlockStatsMap(Map<Block, BlockStats> blockStatsMap) {
        this.blockStatsMap = blockStatsMap;
    }

    public void breakBlock (Player player, Block block){
        BlockStats blockStats = getBlockStats(block);
        blockStats.setHealth(blockStats.getHealth()-1);
        ActionBar.sendActionBar(player  , "block health: " + blockStats.getHealth());
    }

    public boolean checkBlockBreak(Block block){
        BlockStats blockStats = getBlockStats(block);
        return blockStats.getHealth() <= 0;
    }

    public void placeBlock(Player player, Block block){
        BlockStats blockStats = getBlockStats(block);
        blockStats.setBreakable(true);
        blockStats.setBlockType(BlockType.BUILDING);
    }
}
