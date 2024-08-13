package me.orange.anan.player.bed;

import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.mc.util.Position;
import org.bukkit.Location;

@ConfigurationElement
public class BedElement {
    private String bedName = "";
    private Position position = new Position();

    public String getBedName() {
        return bedName;
    }

    public void setBedName(String bedName) {
        this.bedName = bedName;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPosition(Location location) {
        this.position = BukkitPos.toMCPos(location);
    }

    public Location getLocation() {
        return BukkitPos.toBukkitLocation(position);
    }


}
