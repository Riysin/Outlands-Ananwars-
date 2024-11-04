package me.orange.anan.events;

import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeftSafeZoneEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final RegionManager regionManager;

    public PlayerLeftSafeZoneEvent(Player player, RegionManager regionManager) {
        this.player = player;
        this.regionManager = regionManager;
    }

    public Player getPlayer(){
        return this.player;
    }

    public RegionManager getRegionManager() {
        return this.regionManager;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
