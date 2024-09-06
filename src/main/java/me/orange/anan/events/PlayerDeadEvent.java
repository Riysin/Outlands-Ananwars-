package me.orange.anan.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeadEvent extends PlayerDeathEvent {
    private static final HandlerList handlers = new HandlerList();

    public PlayerDeadEvent(Player player) {
        super(player, null, 0,"");
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}