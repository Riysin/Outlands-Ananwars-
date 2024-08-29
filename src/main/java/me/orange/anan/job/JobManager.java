package me.orange.anan.job;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.config.JobElement;
import me.orange.anan.player.config.PlayerConfig;
import me.orange.anan.player.config.PlayerConfigElement;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@InjectableComponent
public class JobManager {
    private final PlayerConfig playerConfig;
    private final JobRegister jobRegister;

    private Map<UUID, JobStats> jobStatsMap = new HashMap<>();

    public JobManager(PlayerConfig playerConfig, JobRegister jobRegister) {
        this.playerConfig = playerConfig;
        this.jobRegister = jobRegister;

        loadConfig();
    }

    public void loadConfig() {
        playerConfig.getPlayerElementMap().forEach((uuid, playerConfigElement) -> {
            if(getJob(playerConfigElement.getJobName()) != null) {
                JobStats jobStats = new JobStats();
                jobStats.setCurrentJob(getJob(playerConfigElement.getJobName()));
                playerConfigElement.getJobLevelMap().forEach((jobName, jobElement) -> {
                    jobStats.getJobLevelMap().put(jobName, jobElement.getLevel());
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
            jobStats.getJobLevelMap().forEach((jobName, level) -> {
                JobElement element = new JobElement();
                element.setLevel(level);
                configElement.getJobLevelMap().put(jobName, element);
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

    public void setPlayerCurrentJob(UUID uuid, Job job) {
        jobStatsMap.get(uuid).setCurrentJob(job);
    }

    public Job getJob(String jobName) {
        return jobRegister.getJobs().stream().filter(job -> job.getName().equals(jobName)).findFirst().orElse(null);
    }

    public boolean hasJob(Player player) {
        return jobStatsMap.get(player.getUniqueId()).getCurrentJob() != null;
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
