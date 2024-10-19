package me.orange.anan.player.job;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import me.orange.anan.events.JobSelectEvent;
import me.orange.anan.events.PlayerLevelUpEvent;
import me.orange.anan.player.job.menu.JobChooseMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

@InjectableComponent
@RegisterAsListener
public class JobEventListener implements Listener {
    private final JobChooseMenu jobChooseMenu;
    private final JobManager jobManager;
    private final NameTagService nameTagService;

    public JobEventListener(JobChooseMenu jobChooseMenu, JobManager jobManager, NameTagService nameTagService) {
        this.jobChooseMenu = jobChooseMenu;
        this.jobManager = jobManager;
        this.nameTagService = nameTagService;
    }

    @EventHandler
    public void onRightClickEnchantingTable(PlayerInteractEvent event) {
        if (event.hasBlock() && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == XMaterial.ENCHANTING_TABLE.parseMaterial()) {
            event.setCancelled(true);
            jobChooseMenu.open(event.getPlayer());
        }
    }

    @EventHandler
    public void onSelect(JobSelectEvent event) {
        Player player = event.getPlayer();
        Job job = event.getJob();

        syncLevel(player);
        player.sendMessage("§eYou have selected §6§l" + job.getName() + " §eas you job!");

        nameTagService.update(MCPlayer.from(player));
    }

    @EventHandler
    public void onUpgrade(PlayerLevelUpEvent event) {
        Player player = event.getPlayer();
        Job job = event.getJob();
        int jobLevel = jobManager.getJobLevel(player, job);

        syncLevel(player);
        player.sendMessage("§eYour §6§l" + job.getName() + " §r§ehas become level §a" + jobLevel + "§e!");
        if(jobLevel == 40) {
            player.sendMessage("§eYou have reached the maximum level of §6§l" + job.getName() + "§r§e!");
            job.active(player, jobLevel);
        }
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event){
        syncLevel(event.getPlayer());
    }

    @EventHandler
    public void onLevelChange(PlayerLevelChangeEvent event){
        syncLevel(event.getPlayer());
    }

    private void syncLevel(Player player) {
        player.setLevel(0);
        player.setExp(0);
        if(jobManager.hasCurrentJob(player)){
            player.setLevel(jobManager.getJobLevel(player, jobManager.geCurrentJob(player)));
        }
    }
}
