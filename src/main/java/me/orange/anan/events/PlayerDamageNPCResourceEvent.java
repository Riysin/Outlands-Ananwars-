package me.orange.anan.events;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDamageNPCResourceEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final NPC npc;

    public PlayerDamageNPCResourceEvent(Player player, NPC npc) {
        this.player = player;
        this.npc = npc;
    }
    public Player getPlayer(){
        return this.player;
    }

    public NPC getNpc() {
        return npc;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}