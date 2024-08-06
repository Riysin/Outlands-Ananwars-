package me.orange.anan.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class PlayerSaveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player eventPlayer;
    private final Player savedPlayer;

    public PlayerSaveEvent(Player eventPlayer, Player savedPlayer) {
        this.eventPlayer = eventPlayer;
        this.savedPlayer = savedPlayer;
    }

    public Player getEventPlayer() {
        return eventPlayer;
    }

    public Player getSavedPlayer() {
        return savedPlayer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
