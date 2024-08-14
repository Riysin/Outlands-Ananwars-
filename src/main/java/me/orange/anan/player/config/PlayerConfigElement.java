package me.orange.anan.player.config;

import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.mc.util.Position;
import org.bukkit.Location;

@ConfigurationElement
public class PlayerConfigElement {
    private int kills;
    private int deaths;
    private Position lastDeathPosition;
    private boolean bossBarActive = false;

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
        this.lastDeathPosition = BukkitPos.toMCPos(location);
    }

    public boolean isBossBarActive() {
        return bossBarActive;
    }

    public void setBossBarActive(boolean bossBarActive) {
        this.bossBarActive = bossBarActive;
    }
}
