package me.orange.anan.events;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDamageNPCResourceEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final NPC npc;
    private final Block block;

    public PlayerDamageNPCResourceEvent(Player player, NPC npc, Block block) {
        this.player = player;
        this.npc = npc;
        this.block = block;
    }

    public Player getPlayer() {
        return this.player;
    }

    public NPC getNpc() {
        return npc;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}