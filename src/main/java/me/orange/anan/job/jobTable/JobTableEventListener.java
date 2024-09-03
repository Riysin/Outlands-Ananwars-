package me.orange.anan.job.jobTable;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import me.orange.anan.events.PlayerChooseJobEvent;
import me.orange.anan.events.PlayerLevelUpEvent;
import me.orange.anan.job.Job;
import me.orange.anan.job.JobManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@InjectableComponent
@RegisterAsListener
public class JobTableEventListener implements Listener {
    private final JobChooseMenu jobChooseMenu;
    private final JobManager jobManager;
    private final NameTagService nameTagService;

    public JobTableEventListener(JobChooseMenu jobChooseMenu, JobManager jobManager, NameTagService nameTagService) {
        this.jobChooseMenu = jobChooseMenu;
        this.jobManager = jobManager;
        this.nameTagService = nameTagService;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.hasBlock() && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == XMaterial.ENCHANTING_TABLE.parseMaterial()) {
            event.setCancelled(true);
            jobChooseMenu.open(event.getPlayer());
        }
    }

    @EventHandler
    public void onChoose(PlayerChooseJobEvent event) {
        Player player = event.getPlayer();
        Job job = event.getJob();
        int jobLevel = jobManager.getPlayerJobLevel(player, job);
        player.sendMessage("§fYou have chosen the §6§l" + job.getName() + " §fjob!");
        player.setLevel(0);
        player.setLevel(jobLevel);

        nameTagService.update(MCPlayer.from(player));
    }

    @EventHandler
    public void onUpgrade(PlayerLevelUpEvent event) {
        Player player = event.getPlayer();
        Job job = event.getJob();
        int jobLevel = jobManager.getPlayerJobLevel(player, job);
        player.sendMessage("§fYour §6§l" + job.getName() + " §fhas become level§a" + jobLevel + "§f!");
        player.setLevel(0);
        player.setLevel(jobLevel);
    }
}
