package me.orange.anan.events;

import me.orange.anan.player.task.Task;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TaskAcceptEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Task task;

    public TaskAcceptEvent(Player player, Task task) {
        this.player = player;
        this.task = task;
    }
    public Player getPlayer(){
        return this.player;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}

