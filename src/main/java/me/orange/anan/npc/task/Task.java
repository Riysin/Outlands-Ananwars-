package me.orange.anan.npc.task;

import org.bukkit.entity.Player;

public abstract class Task {
    protected String id;
    protected String name;
    protected String description;
    protected int goal;
    protected int progress;
    protected TaskStatus status;
    protected String reward;
    protected String rewardDescription;

    public Task(String id, String name, String description, int goal, String reward, String rewardDescription) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.goal = goal;
        this.progress = 0;
        this.status = TaskStatus.UNASSIGNED;
        this.reward = reward;
        this.rewardDescription = rewardDescription;
    }

    public abstract void onProgress(Player player, Object data);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getRewardDescription() {
        return rewardDescription;
    }

    public void setRewardDescription(String rewardDescription) {
        this.rewardDescription = rewardDescription;
    }
}
