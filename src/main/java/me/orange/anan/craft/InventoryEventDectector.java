package me.orange.anan.craft;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.mc.scheduler.MCSchedulers;
import me.orange.anan.events.PlayerOpenInventoryEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;

@InjectableComponent
public class InventoryEventDectector {
    @PostInitialize
    public void init() {
        MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                InventoryView inventory = player.getOpenInventory();
                if (inventory != null && inventory.getType().equals(InventoryType.CRAFTING) &&inventory.getTopInventory().getItem(1) == null) {
                    Bukkit.getPluginManager().callEvent(new PlayerOpenInventoryEvent(player, inventory));
                }
            }
        }, 0, 5);
    }
}
