package me.orange.anan.craft.behaviour.teamCore;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamCore {
    private UUID placePlayer;
    private Creeper coreCreeper;
    private Block coreBlock;

    public TeamCore(UUID uuid, Creeper coreCreeper, Block coreBlock){
        this.placePlayer = uuid;
        this.coreCreeper = coreCreeper;
        this.coreBlock = coreBlock;
    }

    public Creeper getCoreCreeper() {
        return coreCreeper;
    }

    public void setCoreCreeper(Creeper teamCore) {
        this.coreCreeper = teamCore;
    }

    public Block getCoreBlock() {
        return coreBlock;
    }

    public void setCoreBlock(Block coreBlock) {
        this.coreBlock = coreBlock;
    }

    public UUID getPlacePlayer(){
        return this.placePlayer;
    }

    public void setPlacePlayer(Player player){
        this.placePlayer = player.getUniqueId();
    }
}
