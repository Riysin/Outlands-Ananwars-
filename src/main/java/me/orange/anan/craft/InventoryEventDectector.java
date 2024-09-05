package me.orange.anan.craft;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.mc.scheduler.MCSchedulers;
import me.orange.anan.events.PlayerOpenInventoryEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@InjectableComponent
public class InventoryEventDectector {
    @PostInitialize
    public void init() {
        MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getOpenInventory() != null && player.getOpenInventory().getTopInventory().getItem(1) == null) {
                    Bukkit.getPluginManager().callEvent(new PlayerOpenInventoryEvent(player,player.getOpenInventory()));
                }
            }
        }, 0, 3);
    }
}
