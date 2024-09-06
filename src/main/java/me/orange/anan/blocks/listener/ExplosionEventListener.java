package me.orange.anan.blocks.listener;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStats;
import me.orange.anan.blocks.BlockStatsManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

@InjectableComponent
@RegisterAsListener
public class ExplosionEventListener implements Listener {
    private final BlockStatsManager blockStatsManager;

    public ExplosionEventListener(BlockStatsManager blockStatsManager) {
        this.blockStatsManager = blockStatsManager;
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        event.setCancelled(true);

        for (Block block : event.blockList()) {
            Block mainBlock = blockStatsManager.getMainBlock(block);

            if(!blockStatsManager.getBlockStatsMap().containsKey(mainBlock)) {
                continue;
            }

            BlockStats blockStats = blockStatsManager.getBlockStats(mainBlock);
            blockStats.setHealth(blockStats.getHealth() - 10);
            if (blockStats.getHealth() <= 0) {
                blockStatsManager.getBlockStatsMap().remove(mainBlock);
                mainBlock.setType(Material.AIR);
            }

        }

    }
}
