package me.orange.anan.events;

import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftTimer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CraftTimerCountDownEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final CraftTimer craftTimer;

    public CraftTimerCountDownEvent(Player player, CraftTimer craftTimer) {
        this.player = player;
        this.craftTimer = craftTimer;
    }
    public Player getPlayer(){
        return this.player;
    }
    public CraftTimer getCraftTimer(){
        return this.craftTimer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
