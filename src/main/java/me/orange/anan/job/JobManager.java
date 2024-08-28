package me.orange.anan.job;

import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@InjectableComponent
public class JobManager {

    private Map<UUID, JobStats> jobStatsMap = new HashMap<>();

    public JobManager() {
        loadConfig();
    }

    public void loadConfig() {
        // Load config
    }

    public void saveConfig() {
        // Save config
    }

    public Map<UUID, JobStats> getJobStatsMap() {
        return jobStatsMap;
    }

    public void setJobStatsMap(Map<UUID, JobStats> jobStatsMap) {
        this.jobStatsMap = jobStatsMap;
    }

    public Job getPlayerCurrentJob(UUID uuid) {
        return jobStatsMap.get(uuid).getCurrentJob();
    }

    public void setPlayerCurrentJob(UUID uuid, Job job) {
        jobStatsMap.get(uuid).setCurrentJob(job);
    }

    public int getPlayerJobLevel(Player player, Job job) {
        return jobStatsMap.get(player.getUniqueId()).getJobLevelMap().get(job);
    }

    public void addPlayer(Player player, Job job) {
        if (jobStatsMap.containsKey(player.getUniqueId())) return;
        JobStats jobStats = new JobStats();
        jobStats.setCurrentJob(job);
        jobStatsMap.put(player.getUniqueId(), jobStats);
    }

    public void addJobLevel(UUID uuid, Job job) {
        if (jobStatsMap.get(uuid).getJobLevelMap().containsKey(job)) {
            jobStatsMap.get(uuid).getJobLevelMap().put(job, jobStatsMap.get(uuid).getJobLevelMap().get(job) + 1);
        }
    }
}
