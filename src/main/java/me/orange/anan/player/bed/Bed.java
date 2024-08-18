package me.orange.anan.player.bed;


import org.bukkit.Location;

import java.util.UUID;

public class Bed {
    private UUID owner;
    private String bedName;
    private Location location;

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public String getBedName() {
        return bedName;
    }

    public void setBedName(String bedName) {
        this.bedName = bedName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
