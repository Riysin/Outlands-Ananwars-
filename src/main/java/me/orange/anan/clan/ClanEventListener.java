package me.orange.anan.clan;

import io.fairyproject.bukkit.events.player.PlayerDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.event.MCPlayerJoinEvent;
import io.fairyproject.mc.nametag.NameTagService;
import me.orange.anan.events.PlayerJoinClanEvent;
import me.orange.anan.events.PlayerLeftClanEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@RegisterAsListener
@InjectableComponent
public class ClanEventListener implements Listener {
    private final ClanManager clanManager;
    private final NameTagService nameTagService;

    public ClanEventListener(ClanManager clanManager, NameTagService nameTagService) {
        this.clanManager = clanManager;
        this.nameTagService = nameTagService;
    }

    @EventHandler
    public void onJoinTeam(PlayerJoinClanEvent event) {
        nameTagService.update(MCPlayer.from(event.getPlayer()));
        clanManager.setHologram(event.getPlayer());
    }

    @EventHandler
    public void onLeftTeam(PlayerLeftClanEvent event) {
        event.getPlayers().forEach(player -> nameTagService.update(MCPlayer.from(player)));
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
