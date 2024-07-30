package me.orange.anan.craft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CraftTimer {
    private UUID uuid;
    private Craft craft;
    private int time;
    private boolean failed;
    public CraftTimer(Player player, Craft craft, int time) {
        this.uuid= player.getUniqueId();
        this.craft = craft;
        this.time = time+1;
        this.failed = false;
    }
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    public Craft getCraft() {
        return craft;
    }

    public void setCraft(Craft craft) {
        this.craft = craft;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }
}
