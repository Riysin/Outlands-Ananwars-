package me.orange.anan.player.job;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.events.PlayerChooseJobEvent;
import me.orange.anan.events.PlayerLevelUpEvent;
import me.orange.anan.player.config.JobElement;
import me.orange.anan.player.config.PlayerConfig;
import me.orange.anan.player.config.PlayerConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@InjectableComponent
public class JobManager {
    private final PlayerConfig playerConfig;
    private final JobRegistry jobRegistry;

    private Map<UUID, JobStats> jobStatsMap = new HashMap<>();

    public JobManager(PlayerConfig playerConfig, JobRegistry jobRegistry) {
        this.playerConfig = playerConfig;
        this.jobRegistry = jobRegistry;

        loadConfig();
    }

    public void loadConfig() {
        playerConfig.getPlayerElementMap().forEach((uuid, playerConfigElement) -> {
            if(getJob(playerConfigElement.getJobName()) != null) {
                JobStats jobStats = new JobStats();
                jobStats.setCurrentJob(getJob(playerConfigElement.getJobName()));
                playerConfigElement.getJobLevelMap().forEach((jobID, jobElement) -> {
                    jobStats.getJobLevelMap().put(jobID, jobElement.getLevel());
                });
                jobStatsMap.put(UUID.fromString(uuid), jobStats);
            }
        });
    }

    public void saveConfig() {
        jobStatsMap.forEach((uuid, jobStats) -> {
            PlayerConfigElement configElement = playerConfig.getPlayerElementMap().get(uuid.toString());

            configElement.getJobLevelMap().clear();
            configElement.setJobName(jobStats.getCurrentJob().getName());
            jobStats.getJobLevelMap().forEach((jobID, level) -> {
                JobElement element = new JobElement();
                element.setLevel(level);
                configElement.getJobLevelMap().put(jobID, element);
            });
        });
        playerConfig.save();
    }

    public Map<UUID, JobStats> getJobStatsMap() {
        return jobStatsMap;
    }

    public Job getPlayerCurrentJob(UUID uuid) {
        if(jobStatsMap.containsKey(uuid))
            return jobStatsMap.get(uuid).getCurrentJob();
        return null;
    }

    public Job getPlayerCurrentJob(Player player) {
        return getPlayerCurrentJob(player.getUniqueId());
    }

    public void setPlayerCurrentJob(UUID uuid, Job job) {
        jobStatsMap.get(uuid).setCurrentJob(job);
    }

    public Job getJob(String jobName) {
        return jobRegistry.getJobs().stream().filter(job -> job.getName().equals(jobName)).findFirst().orElse(null);
    }

    public Job getJobByID(String jobID) {
        return jobRegistry.getJobs().stream().filter(job -> job.getID().equals(jobID)).findFirst().orElse(null);
    }

    public boolean hasJob(Player player) {
        return jobStatsMap.get(player.getUniqueId()).getCurrentJob() != null;
    }

    public Map<String, Integer> getPlayerJobLevelMap(Player player) {
        return jobStatsMap.get(player.getUniqueId()).getJobLevelMap();
    }

    public int getPlayerJobLevel(Player player, Job job) {
        return jobStatsMap.get(player.getUniqueId()).getJobLevelMap().get(job.getID());
    }

    public void addPlayer(Player player, Job job) {
        UUID uuid = player.getUniqueId();
        if (!jobStatsMap.containsKey(uuid))
            jobStatsMap.put(uuid, new JobStats());
        JobStats jobStats = jobStatsMap.get(uuid);
        jobStats.setCurrentJob(job);
        if(!jobStats.getJobLevelMap().containsKey(job.getID()))
            jobStats.getJobLevelMap().put(job.getID(), 0);
        Bukkit.getPluginManager().callEvent(new PlayerChooseJobEvent(Bukkit.getPlayer(uuid), job));
    }

    public void addJobLevel(UUID uuid, Job job) {
        Map<String, Integer> levelMap = jobStatsMap.get(uuid).getJobLevelMap();
        if (levelMap.containsKey(job.getID())) {
            levelMap.replace(job.getID(), levelMap.get(job.getID()) + 1);
            Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(Bukkit.getPlayer(uuid), job));
        }
    }

    public void removePlayer(Player player, Job job) {
        jobStatsMap.get(player.getUniqueId()).getJobLevelMap().remove(job.getID());
        jobStatsMap.get(player.getUniqueId()).setCurrentJob(null);
    }
}
