package me.orange.anan.job;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.events.PlayerChooseJobEvent;
import me.orange.anan.events.PlayerLevelUpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@InjectableComponent
@RegisterAsListener
public class JobEventListener implements Listener {
    private final JobManager jobManager;

    public JobEventListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onChoose(PlayerChooseJobEvent event) {
        Player player = event.getPlayer();
        Job job = event.getJob();
        int jobLevel = jobManager.getPlayerJobLevel(player, job);
        player.sendMessage("§fYou have chosen the §6§l" + job.getName() + " §fjob!");
        player.setLevel(jobLevel);
    }

    @EventHandler
    public void onUpgrade(PlayerLevelUpEvent event) {
        Player player = event.getPlayer();
        Job job = event.getJob();
        int jobLevel = jobManager.getPlayerJobLevel(player, job);
        player.sendMessage("§fYour §6§l" + job.getName() + " §fhas become level§a" + jobLevel + "§f!");
        player.setLevel(jobLevel);
    }
}
