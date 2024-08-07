package me.orange.anan.craft.behaviour.teamCore;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamCore {
    private Creeper teamCore;
    private Block coreBlock;
    private UUID placePlayer;

    public TeamCore(UUID uuid, Creeper teamCore, Block coreBlock){
        this.coreBlock = coreBlock;
        this.teamCore = teamCore;
        this.placePlayer = uuid;
    }

    public Creeper getTeamCore() {
        return teamCore;
    }

    public void setTeamCore(Creeper teamCore) {
        this.teamCore = teamCore;
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
