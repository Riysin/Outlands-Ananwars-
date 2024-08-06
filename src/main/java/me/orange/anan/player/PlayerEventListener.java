package me.orange.anan.player;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import me.orange.anan.craft.crafting.CraftTimerManager;
import me.orange.anan.player.config.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;

@InjectableComponent
@RegisterAsListener
public class PlayerEventListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final CraftTimerManager craftTimerManager;
    private final NameTagService nameTagService;
    private final PlayerConfig playerConfig;

    public PlayerEventListener(PlayerDataManager playerDataManager, CraftTimerManager craftTimerManager, NameTagService nameTagService, PlayerConfig playerConfig) {
        this.playerDataManager = playerDataManager;
        this.craftTimerManager = craftTimerManager;
        this.nameTagService = nameTagService;
        this.playerConfig = playerConfig;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerDataManager.setUpPlayer(event);

        Bukkit.getOnlinePlayers().forEach(player -> {
            nameTagService.update(MCPlayer.from(player));
        });

    }

    @EventHandler
    public void onEntityKilled(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null)
            return;
        if (killer.getType() == EntityType.PLAYER) {
            playerConfig.addPlayerKills(killer.getName());
        }
    }

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent event) {
        event.setQuitMessage("§e" + event.getPlayer().getName() + " has left!");
        craftTimerManager.getPlayerCraftTimerList(event.getPlayer()).forEach(craftTimer -> {
            craftTimerManager.craftingFailed(event.getPlayer(), craftTimer);
        });
    }
}
