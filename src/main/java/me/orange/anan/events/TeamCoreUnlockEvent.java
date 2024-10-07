package me.orange.anan.events;

import me.orange.anan.craft.behaviour.teamCore.TeamCore;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamCoreUnlockEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final TeamCore teamCore;

    public TeamCoreUnlockEvent(Player player, TeamCore teamCore) {
        this.player = player;
        this.teamCore = teamCore;
    }

    public Player getPlayer() {
        return this.player;
    }

    public TeamCore getTeamCore() {
        return this.teamCore;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
