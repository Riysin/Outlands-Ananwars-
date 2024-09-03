package me.orange.anan.player.death;

import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class DeathLoot {
    private String ownerName;
    private UUID owner;
    private Inventory inventory;

    public DeathLoot(String ownerName, UUID owner, Inventory inventory) {
        this.ownerName = ownerName;
        this.owner = owner;
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

}
