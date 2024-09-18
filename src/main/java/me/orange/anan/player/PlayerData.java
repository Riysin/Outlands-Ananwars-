package me.orange.anan.player;

import io.fairyproject.mc.tablist.util.Skin;
import me.orange.anan.player.task.Task;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Location;

import java.util.*;

public class PlayerData {
    //config
    private int kills = 0;
    private int deaths = 0;
    private Location lastDeathLocation;
    private boolean bossBarActive = false;
    private List<UUID> friends = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();
    //stats
    private List<UUID> friendRequests = new ArrayList<>();
    private BossBar BossBar;
    private boolean npcDied = false;
    private Skin skin = Skin.GRAY;
    private Set<String> canCraftItems = new HashSet<>();

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

    public void addDeath() {
        deaths++;
    }

    public void addKill() {
        kills++;
    }

    public boolean isNpcDied() {
        return npcDied;
    }

    public void setNpcDied(boolean npcDied) {
        this.npcDied = npcDied;
    }

    public Location getLastDeathLocation() {
        return lastDeathLocation;
    }

    public void setLastDeathLocation(Location lastDeathLocation) {
        this.lastDeathLocation = lastDeathLocation;
    }

    public BossBar getBossBar() {
        return BossBar;
    }

    public void setBossBar(BossBar bossBar) {
        this.BossBar = bossBar;
    }

    public boolean isBossBarActive() {
        return bossBarActive;
    }

    public void setBossBarActive(boolean bossBarActive) {
        this.bossBarActive = bossBarActive;
    }

    public List<UUID> getFriends() {
        return friends;
    }

    public List<UUID> getOnlineFriends() {
        List<UUID> onlineFriends = new ArrayList<>();
        for (UUID friend : friends) {
            if (friend != null) {
                onlineFriends.add(friend);
            }
        }
        return onlineFriends;
    }

    public void setFriends(List<UUID> friends) {
        this.friends = friends;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<UUID> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<UUID> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public Set<String> getCanCraftItems() {
        return canCraftItems;
    }

    public void setCanCraftItems(Set<String> canCraftItems) {
        this.canCraftItems = canCraftItems;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

}
