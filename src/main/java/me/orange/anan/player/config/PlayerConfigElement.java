package me.orange.anan.player.config;

import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.mc.util.Position;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@ConfigurationElement
public class PlayerConfigElement {
    private int kills;
    private int deaths;
    private Position lastDeathPosition = new Position();
    private boolean bossBarActive = false;
    @ElementType(BedElement.class)
    private List<BedElement> bedList = new ArrayList<>();

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public Position getLastDeathPosition() {
        return lastDeathPosition;
    }

    public Location getLastDeathLocation() {
        return BukkitPos.toBukkitLocation(lastDeathPosition);
    }

    public void setLastDeathPosition(Position position) {
        this.lastDeathPosition = position;
    }

    public void setLastDeathLocation(Location location) {
        if (location == null) return;
        this.lastDeathPosition = BukkitPos.toMCPos(location);
    }

    public boolean isBossBarActive() {
        return bossBarActive;
    }

    public void setBossBarActive(boolean bossBarActive) {
        this.bossBarActive = bossBarActive;
    }

    public List<BedElement> getBedList() {
        return bedList;
    }

    public void setBedList(List<BedElement> bedList) {
        this.bedList = bedList;
    }
}
