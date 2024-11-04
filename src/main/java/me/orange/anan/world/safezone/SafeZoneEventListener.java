package me.orange.anan.world.safezone;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@InjectableComponent
@RegisterAsListener
public class SafeZoneEventListener implements Listener {
    private final SafeZoneManager safeZoneManager;

    public SafeZoneEventListener(SafeZoneManager safeZoneManager) {
        this.safeZoneManager = safeZoneManager;
    }

    @EventHandler
    public void onAnimalAttack(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Animals && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Animals animals = (Animals) event.getDamager();

            if (safeZoneManager.isInSafeZone(player)) {
                animals.remove();
                event.setCancelled(true);
            }
        }
    }
}
