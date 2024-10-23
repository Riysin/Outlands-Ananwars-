package me.orange.anan.blocks.listener;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStats;
import me.orange.anan.blocks.BlockStatsManager;
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
        // Check if the damage is caused by an explosion
        if (event.getCause() == EntityDamageByEntityEvent.DamageCause.ENTITY_EXPLOSION ||
                event.getCause() == EntityDamageByEntityEvent.DamageCause.BLOCK_EXPLOSION) {

            Entity damagedEntity = event.getEntity();
            Entity damager = event.getDamager();

            if (damagedEntity instanceof LivingEntity) {
                // Check if there's a wall between the explosion and the entity
                if (isBlockedByWall(damager, damagedEntity)) {
                    // Cancel the damage if there's a wall between
                    event.setCancelled(true);
                }
            }
        }
    }

    // This method checks if there's a block between the explosion and the entity
    private boolean isBlockedByWall(Entity explosionSource, Entity entity) {
        Vector sourceLocation = explosionSource.getLocation().toVector();
        Vector entityLocation = entity.getLocation().toVector();
        Vector direction = entityLocation.subtract(sourceLocation).normalize();

        double distance = sourceLocation.distance(entityLocation);

        for (double i = 0; i < distance; i += 0.5) {
            Vector currentPosition = sourceLocation.clone().add(direction.clone().multiply(i));
            Block block = explosionSource.getWorld().getBlockAt(currentPosition.getBlockX(), currentPosition.getBlockY(), currentPosition.getBlockZ());

            // If there's a solid block between the explosion and the entity, return true
            if (block.getType() != Material.AIR && block.getType().isSolid()) {
                return true;
            }
        }

        return false;
    }
}
