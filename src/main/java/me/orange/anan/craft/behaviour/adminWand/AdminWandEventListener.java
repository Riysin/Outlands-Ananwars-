package me.orange.anan.craft.behaviour.adminWand;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.events.AdminRightClickWandEvent;
import me.orange.anan.events.AdminShiftRightClickWandEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@InjectableComponent
@RegisterAsListener
public class AdminWandEventListener implements Listener {
    private final AdminWandMenu adminWandMenu;
    private final AdminWandManager adminWandManager;

    public AdminWandEventListener(AdminWandMenu adminWandMenu, AdminWandManager adminWandManager) {
        this.adminWandMenu = adminWandMenu;
        this.adminWandManager = adminWandManager;
    }

    @EventHandler
    public void onRightClickAdminWand(AdminRightClickWandEvent event) {
        Player player = event.getPlayer();
        player.performCommand(adminWandManager.getAction(player).getCommand());
    }

    @EventHandler
    public void onSneakRightClickAdminWand(AdminShiftRightClickWandEvent event) {
        adminWandMenu.open(event.getPlayer());
    }
}
