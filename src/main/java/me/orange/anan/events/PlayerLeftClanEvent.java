package me.orange.anan.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class PlayerLeftClanEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final List<Player> players;

    public PlayerLeftClanEvent(List<Player> players) {
        this.players = getPlayers();
    }
    public List<Player> getPlayers(){
        return this.players;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
