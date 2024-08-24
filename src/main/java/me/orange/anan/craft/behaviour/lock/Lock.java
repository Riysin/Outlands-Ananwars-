package me.orange.anan.craft.behaviour.lock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class Lock {
    private UUID owner;
    private Location location;

    public UUID getOwner() {
        return owner;
    }

    public OfflinePlayer getOfflineOwner(){
        return Bukkit.getOfflinePlayer(owner);
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
