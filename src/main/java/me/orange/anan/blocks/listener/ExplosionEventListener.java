package me.orange.anan.blocks.listener;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStats;
import me.orange.anan.blocks.BlockStatsManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

@InjectableComponent
@RegisterAsListener
public class ExplosionEventListener implements Listener {
    private final BlockStatsManager blockStatsManager;

    public ExplosionEventListener(BlockStatsManager blockStatsManager) {
        this.blockStatsManager = blockStatsManager;
    }

    @EventHandler
    public void onTNTPlaced(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.TNT) {
            event.setCancelled(true);
            Entity tnt = block.getWorld().spawnEntity(block.getLocation().add(0.5, 0, 0.5), EntityType.PRIMED_TNT);
            TNTPrimed tntPrimed = (TNTPrimed) tnt;
            tntPrimed.setFuseTicks(80);
        }
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        event.setCancelled(true);

        for (Block block : event.blockList()) {
            Block mainBlock = blockStatsManager.getMainBlock(block);

            if (!blockStatsManager.getBlockStatsMap().containsKey(mainBlock)) {
                continue;
            }

            BlockStats blockStats = blockStatsManager.getBlockStats(mainBlock);
            blockStats.setHealth(blockStats.getHealth() - 100);
            if (blockStats.getHealth() <= 0) {
                blockStatsManager.getBlockStatsMap().remove(mainBlock);
                mainBlock.setType(Material.AIR);
            }

        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageByEntityEvent.DamageCause.ENTITY_EXPLOSION ||
                event.getCause() == EntityDamageByEntityEvent.DamageCause.BLOCK_EXPLOSION) {

            Entity damagedEntity = event.getEntity();
            Entity damager = event.getDamager();

            if (damagedEntity instanceof LivingEntity) {
                if (isBlockedByWall(damager, (LivingEntity) damagedEntity)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public boolean isBlockedByWall(Entity damager, LivingEntity damagedEntity) {
        Location damagerLocation = damager.getLocation().add(0.5,0.5,0.5);
        Location damagedLocation = damagedEntity.getEyeLocation();

        Vector direction = damagedLocation.toVector().subtract(damagerLocation.toVector()).normalize();
        double distance = damagerLocation.distance(damagedLocation);
        double stepSize = 0.5;

        // Iterate through the line between the damager and the damaged entity
        for (double d = 0; d < distance; d += stepSize) {
            Location currentLocation = damagerLocation.clone().add(direction.clone().multiply(d));

            if (currentLocation.getBlock().getType().isSolid()) {
                return true; // Block (wall) is in the way
            }
        }

        return false; // No blocks were found in the way
    }
}
