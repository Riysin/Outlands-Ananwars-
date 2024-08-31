package me.orange.anan.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class PlayerOpenInventoryEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Inventory topInventory;
    private final Inventory bottomInventory;

    public PlayerOpenInventoryEvent(Player player, InventoryView inventory) {
        this.player = player;
        this.topInventory = inventory.getTopInventory();
        this.bottomInventory = inventory.getBottomInventory();
    }

    public Player getPlayer() {
        return this.player;
    }

    public Inventory getTopInventory() {
        return topInventory;
    }

    public Inventory getBottomInventory() {
        return bottomInventory;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
