package me.orange.anan.player;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import me.orange.anan.craft.crafting.CraftTimerManager;
import me.orange.anan.player.config.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;

import java.util.HashSet;
import java.util.Set;

@InjectableComponent
@RegisterAsListener
public class PlayerEventListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final CraftTimerManager craftTimerManager;
    private final NameTagService nameTagService;

    public PlayerEventListener(PlayerDataManager playerDataManager, CraftTimerManager craftTimerManager, NameTagService nameTagService) {
        this.playerDataManager = playerDataManager;
        this.craftTimerManager = craftTimerManager;
        this.nameTagService = nameTagService;
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
            playerDataManager.getPlayerData(killer).addKill();
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
