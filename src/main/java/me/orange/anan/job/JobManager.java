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

    public Job getPlayerCurrentJob(UUID uuid) {
        return jobStatsMap.get(uuid).getCurrentJob();
    }

    public void setPlayerCurrentJob(UUID uuid, Job job) {
        jobStatsMap.get(uuid).setCurrentJob(job);
    }

    public int getPlayerJobLevel(Player player, Job job) {
        return jobStatsMap.get(player.getUniqueId()).getJobLevelMap().get(job.getName());
    }

    public void addPlayer(Player player, Job job) {
        UUID uuid = player.getUniqueId();
        if (!jobStatsMap.containsKey(uuid))
            jobStatsMap.put(uuid, new JobStats());
        JobStats jobStats = jobStatsMap.get(uuid);
        jobStats.setCurrentJob(job);
        if(!jobStats.getJobLevelMap().containsKey(job.getName()))
            jobStats.getJobLevelMap().put(job.getName(), 0);
    }

    public void addJobLevel(UUID uuid, Job job) {
        if (jobStatsMap.get(uuid).getJobLevelMap().containsKey(job.getName())) {
            jobStatsMap.get(uuid).getJobLevelMap().replace(job.getName(), jobStatsMap.get(uuid).getJobLevelMap().get(job.getName()) + 1);
        }
    }
}
