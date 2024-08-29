package me.orange.anan.events;

import me.orange.anan.job.Job;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLevelUpEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Job job;

    public PlayerLevelUpEvent(Player player, Job job) {
        this.player = player;
        this.job = job;
    }
    public Player getPlayer(){
        return this.player;
    }

    public Job getJob(){
        return this.job;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
