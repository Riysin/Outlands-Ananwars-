package me.orange.anan.player.job;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.events.JobSelectEvent;
import me.orange.anan.events.PlayerLevelUpEvent;
import me.orange.anan.player.config.JobElement;
import me.orange.anan.player.config.PlayerConfig;
import me.orange.anan.player.config.PlayerConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
            JobStats jobStats = new JobStats();
            playerConfigElement.getJobLevelMap().forEach((jobID, jobElement) -> {
                jobStats.getJobLevelMap().put(jobID, jobElement.getLevel());
            });

            if (!playerConfigElement.getJobId().isEmpty()) {
                jobStats.setCurrentJob(getJobByID(playerConfigElement.getJobId()));
            }

            jobStatsMap.put(UUID.fromString(uuid), jobStats);
        });
    }

    public void saveConfig() {
        jobStatsMap.forEach((uuid, jobStats) -> {
            PlayerConfigElement configElement = playerConfig.getPlayerConfigElement(uuid);

            configElement.getJobLevelMap().clear();

            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            configElement.setJobId(hasCurrentJob(player) ? getCurrentJob(player).getID() : "");
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

    public Job getCurrentJob(UUID uuid) {
        if (jobStatsMap.containsKey(uuid))
            return jobStatsMap.get(uuid).getCurrentJob();
        return null;
    }

    public Job getCurrentJob(OfflinePlayer player) {
        return getCurrentJob(player.getUniqueId());
    }

    public void setCurrentJob(UUID uuid, Job job) {
        jobStatsMap.get(uuid).setCurrentJob(job);
    }

    public boolean hasCurrentJob(OfflinePlayer player) {
        return jobStatsMap.get(player.getUniqueId()).getCurrentJob() != null;
    }

    public Job getJobByID(String jobID) {
        return jobRegistry.getJobs().stream().filter(job -> job.getID().equals(jobID)).findFirst().orElse(null);
    }


    public boolean hasUnlockJob(OfflinePlayer player, Job job) {
        return jobStatsMap.get(player.getUniqueId()).getJobLevelMap().containsKey(job.getID());
    }

    public Map<String, Integer> getJobLevelMap(Player player) {
        return jobStatsMap.get(player.getUniqueId()).getJobLevelMap();
    }

    public int getJobLevel(Player player, Job job) {
        return jobStatsMap.get(player.getUniqueId()).getJobLevelMap().get(job.getID());
    }

    public void addPlayer(Player player, Job job) {
        UUID uuid = player.getUniqueId();
        if (!jobStatsMap.containsKey(uuid))
            jobStatsMap.put(uuid, new JobStats());
        JobStats jobStats = jobStatsMap.get(uuid);
        jobStats.setCurrentJob(job);
        if (!jobStats.getJobLevelMap().containsKey(job.getID()))
            jobStats.getJobLevelMap().put(job.getID(), 0);
        Bukkit.getPluginManager().callEvent(new JobSelectEvent(Bukkit.getPlayer(uuid), job));
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
