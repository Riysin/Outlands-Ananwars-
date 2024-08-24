package me.orange.anan.craft.behaviour.lock;

import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.mc.util.Position;
import org.bukkit.Location;

import java.util.UUID;

@ConfigurationElement
public class LockConfigElement {
    private String owner = "";
    private Position position = new Position();

    public UUID getOwner() {
        return UUID.fromString(owner);
    }

    public void setOwner(UUID owner) {
        this.owner = owner.toString();
    }

    public Location getLocation() {
        return BukkitPos.toBukkitLocation(position);
    }

    public void setLocation(Location location) {
        this.position = BukkitPos.toMCPos(location);
    }
}
