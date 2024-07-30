package me.orange.anan.clan;

import io.fairyproject.bukkit.events.player.PlayerDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@RegisterAsListener
@InjectableComponent
public class ClanEventListener implements Listener {
    private final ClanManager clanManager;

    public ClanEventListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onPVP(PlayerDamageByPlayerEvent event) {
        Player damager = event.getDamager();
        Player player = event.getPlayer();

        if (clanManager.getPlayerClan(player) != null && clanManager.getPlayerClan(damager) != null) {
            if (clanManager.sameClan(player, damager)) {
                event.setCancelled(true);
                damager.sendMessage("§cYou are in the same clan");
            }
        }
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        if (clanManager.inClan(event.getPlayer())) {
            event.setCancelled(true);
            clanManager.getPlayerClan(event.getPlayer()).getOnlineBukkitPlayers().forEach(player -> {
                player.sendMessage("§3[Team Chat]§r " + event.getPlayer().getName() + " : " + event.getMessage());
            });
        }
    }
}
