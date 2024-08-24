package me.orange.anan.craft.behaviour.teamCore;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TeamCore {
    private UUID placePlayer;
    private Creeper coreCreeper;
    private Block coreBlock;
    private Set<Block> connectedBlocks;
    private Inventory inventory;

    public TeamCore(UUID uuid, Creeper creeper, Block coreBlock) {
        this.placePlayer = uuid;
        this.coreCreeper = creeper;
        this.coreBlock = coreBlock;
        this.connectedBlocks = new HashSet<>();
        this.inventory = Bukkit.createInventory(null, 9*4, "§6隊伍核心");
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

    public UUID getPlacePlayer() {
        return this.placePlayer;
    }

    public OfflinePlayer getOfflinePlacePlayer() {
        return Bukkit.getOfflinePlayer(this.placePlayer);
    }

    public void setPlacePlayer(Player player) {
        this.placePlayer = player.getUniqueId();
    }

    public Set<Block> getConnectedBlocks() {
        return connectedBlocks;
    }

    public void addConnectedBlock(Block block) {
        connectedBlocks.add(block);
        System.out.println("Added block to connectedBlocks: " + block.getType());
    }

    public void setConnectedBlocks(Set<Block> connectedBlocks) {
        this.connectedBlocks.clear();
        this.connectedBlocks.addAll(connectedBlocks);
    }

    public Set<Block> getTerritoryBlocks() {
        Set<Block> territoryBlocks = new HashSet<>();
        for (Block connectedBlock : this.connectedBlocks) {
            for (int i = 0; i <= 3; i++) {
                Block aboveBlock = connectedBlock.getRelative(0, i, 0);
                territoryBlocks.add(aboveBlock);
            }
        }
        return territoryBlocks;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
