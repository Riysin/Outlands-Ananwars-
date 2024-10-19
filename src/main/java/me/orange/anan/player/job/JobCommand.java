package me.orange.anan.player.job;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.events.JobSelectEvent;
import me.orange.anan.events.PlayerLevelUpEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@InjectableComponent
@Command(value = {"j","job"},permissionNode = "job.admin")
public class JobCommand extends BaseCommand {
    private final JobManager jobManager;

    public JobCommand(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Command("add")
    public void addJob(BukkitCommandContext ctx,@Arg("Player") Player player , @Arg("job") Job job) {
        Player ctxPlayer = ctx.getPlayer();
        if(!jobManager.getJobStatsMap().containsKey(player.getUniqueId())){
            jobManager.getJobStatsMap().put(player.getUniqueId(), new JobStats());
        }
        jobManager.getJobLevelMap(player).put(job.getID(), 0);
        player.sendMessage("Job added");
    }

    @Command(value = {"select"},permissionNode = "job.admin")
    public void setJob(BukkitCommandContext ctx, @Arg("player") Player player, @Arg("job") Job job) {
        jobManager.addPlayer(player, job);
    }

    @Command(value = {"level"},permissionNode = "job.admin")
    public void setLevel(BukkitCommandContext ctx, @Arg("player") Player player, @Arg("level") int level) {
        if (jobManager.getJobStatsMap().get(player.getUniqueId()).getCurrentJob() == null) {
            ctx.getPlayer().sendMessage("Player has no job");
            return;
        }
        JobStats jobStats = jobManager.getJobStatsMap().get(player.getUniqueId());
        Job job = jobStats.getCurrentJob();
        jobStats.getJobLevelMap().replace(job.getID(), level);
        Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(player, job));
    }

    @Command(value = {"remove"},permissionNode = "job.admin")
    public void setRemove(BukkitCommandContext ctx, @Arg("player") Player player, @Arg("job") Job job) {
        jobManager.removePlayer(player, job);
        ctx.getPlayer().sendMessage("Job removed");
    }

    @Command(value = {"list"},permissionNode = "job.admin")
    public void list(BukkitCommandContext ctx) {
        ctx.getPlayer().sendMessage("Jobs: ");
        for (String id : jobManager.getJobStatsMap().get(ctx.getPlayer().getUniqueId()).getJobLevelMap().keySet()) {
            ctx.getPlayer().sendMessage(jobManager.getJobByID(id).getName());
        }
    }

    @Command(value = {"resign "})
    public void resign(BukkitCommandContext ctx) {
        jobManager.setCurrentJob(ctx.getPlayer().getUniqueId(), null);
        ctx.getPlayer().sendMessage("You have resigned from your job");
    }
}
