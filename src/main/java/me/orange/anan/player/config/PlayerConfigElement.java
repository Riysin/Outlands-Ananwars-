package me.orange.anan.player.config;

import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.mc.util.Position;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationElement
public class PlayerConfigElement {
    private String name = "";
    private int kills = 0;
    private int deaths = 0;
    private Position lastDeathPosition = new Position();
    private boolean bossBarActive = false;
    @ElementType(BedElement.class)
    private List<BedElement> bedList = new ArrayList<>();
    private String jobId = "";
    @ElementType(JobElement.class)
    private Map<String,JobElement> jobLevelMap = new HashMap<>();
    @ElementType(FriendElement.class)
    private List<FriendElement> friendList = new ArrayList<>();
    @ElementType(TaskElement.class)
    private Map<String, TaskElement> taskElementMap = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Map<String, JobElement> getJobLevelMap() {
        return jobLevelMap;
    }

    public void setJobLevelMap(Map<String, JobElement> jobLevelMap) {
        this.jobLevelMap = jobLevelMap;
    }

    public List<FriendElement> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<FriendElement> friendList) {
        this.friendList = friendList;
    }

    public Map<String, TaskElement> getTaskElementMap() {
        return taskElementMap;
    }

    public void setTaskElementMap(Map<String, TaskElement> taskElementMap) {
        this.taskElementMap = taskElementMap;
    }
}
