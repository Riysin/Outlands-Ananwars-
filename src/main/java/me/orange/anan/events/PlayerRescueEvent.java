package me.orange.anan.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRescueEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player rescuer;
    private final Player rescuedPlayer;
    private boolean isCancelled;

    public PlayerRescueEvent(Player rescuer, Player rescuedPlayer) {
        this.rescuer = rescuer;
        this.rescuedPlayer = rescuedPlayer;
    }

    public Player getRescuer() {
        return rescuer;
    }

    public Player getRescuedPlayer() {
        return rescuedPlayer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }
}
