package me.orange.anan.job.jobTable;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

@InjectableComponent
@RegisterAsListener
public class JobTableEventListener implements Listener {
    private final JobChooseMenu jobChooseMenu;

    public JobTableEventListener(JobChooseMenu jobChooseMenu) {
        this.jobChooseMenu = jobChooseMenu;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getClickedBlock().getType() == XMaterial.ENCHANTING_TABLE.parseMaterial()) {
            event.setCancelled(true);
            jobChooseMenu.open(event.getPlayer());
        }
    }
}
