package me.orange.anan.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPlaceTeamCoreEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Block block;
    private boolean isCancelled;

    public PlayerPlaceTeamCoreEvent(Player player, Block block) {
        this.player = player;
        this.block = block;
    }
    public Player getPlayer(){
        return this.player;
    }

    public Block getPlaceBlock(){
        return this.block;
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
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }
}
