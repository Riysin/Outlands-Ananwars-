package me.orange.anan.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class BlockResourceBreakEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Block block;

    public BlockResourceBreakEvent(Player player, Block block) {
        this.player = player;
        this.block = block;
    }
    public Player getPlayer(){
        return this.player;
    }

    public Block getBlock(){
        return this.block;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}