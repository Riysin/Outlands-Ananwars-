package me.orange.anan;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.server.ServerListPingEvent;

@InjectableComponent
@RegisterAsListener
public class GeneralEventListener implements Listener {
    @EventHandler
    public void ServerListPingEvent(ServerListPingEvent event) {
        event.setMotd("§b§lAnan§f§lWars §r§7| §e§lby §6§l@PvpForOrange\n      §f❥ §b§l§n1.8 Available!");
        event.setMaxPlayers(134);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
