package me.orange.anan;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.craft.behaviour.teamCore.TeamCoreManager;
import me.orange.anan.util.ItemLoreBuilder;
import me.orange.anan.world.resource.OreClusterPopulator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
@RegisterAsListener
public class GeneralEventListener implements Listener {
    private final TeamCoreManager teamCoreManager;
    private final BlockStatsManager blockStatsManager;
    private final CraftManager craftManager;

    public GeneralEventListener(TeamCoreManager teamCoreManager, BlockStatsManager blockStatsManager, CraftManager craftManager) {
        this.teamCoreManager = teamCoreManager;
        this.blockStatsManager = blockStatsManager;
        this.craftManager = craftManager;
    }

    @EventHandler
    public void ServerListPingEvent(ServerListPingEvent event) {
        event.setMotd("§b§lOut§f§lLands §r§7| §e§lAnanWars!\n      §f❥ §6§l§n1.8 Available!");
        event.setMaxPlayers(134);
    }

    @EventHandler
    public void populateChunk(WorldInitEvent event) {
        Bukkit.broadcastMessage("Populating chunk");
        event.getWorld().getPopulators().add(new OreClusterPopulator(teamCoreManager, blockStatsManager));
    }

    @EventHandler
    public void onEntityKilled(EntityDeathEvent event) {
        event.setDroppedExp(0);
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            event.getDrops().forEach(drop -> {
                ItemStack itemStack = craftManager.getItemStack(drop, player);
                player.getInventory().addItem(itemStack);
                player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
            });
            event.getDrops().clear();
        }
    }

    @EventHandler
    public void onSwim(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        Material blockType = location.getBlock().getType();

        if (blockType == Material.WATER || blockType == Material.STATIONARY_WATER) {
            if (player.isSneaking()) {
                player.setVelocity(location.getDirection().multiply(0.5));
            }
        }
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        if (craftManager.getCraft(item) == null) {
            event.setCancelled(true);
        }
    }
}
