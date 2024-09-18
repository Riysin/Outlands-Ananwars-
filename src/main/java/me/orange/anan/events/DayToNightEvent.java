package me.orange.anan.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DayToNightEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public DayToNightEvent() {
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}