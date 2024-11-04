package me.orange.anan.player;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import io.fairyproject.mc.scheduler.MCSchedulers;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.craft.crafting.CraftTimerManager;
import me.orange.anan.player.death.DeathRespawnMenu;
import me.orange.anan.player.job.JobManager;
import me.orange.anan.player.job.JobStats;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

@InjectableComponent
@RegisterAsListener
public class PlayerEventListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final CraftTimerManager craftTimerManager;
    private final NameTagService nameTagService;
    private final JobManager jobManager;
    private final ClanManager clanManager;
    private final DeathRespawnMenu deathRespawnMenu;

    public PlayerEventListener(PlayerDataManager playerDataManager, CraftTimerManager craftTimerManager, NameTagService nameTagService, JobManager jobManager, ClanManager clanManager, DeathRespawnMenu deathRespawnMenu) {
        this.playerDataManager = playerDataManager;
        this.craftTimerManager = craftTimerManager;
        this.nameTagService = nameTagService;
        this.jobManager = jobManager;
        this.clanManager = clanManager;
        this.deathRespawnMenu = deathRespawnMenu;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage(null);
        playerDataManager.setUpPlayer(event);
        jobManager.getJobStatsMap().putIfAbsent(player.getUniqueId(), new JobStats());

        playerDataManager.getFriends(player).forEach(friend -> {
            if (Bukkit.getOfflinePlayer(friend).isOnline()) {
                Bukkit.getPlayer(friend).sendMessage("§eYour friend §6" + player.getName() + " §eis now online!");
            }
        });

        if (playerDataManager.getPlayerData(player).isNpcDied()) {
            playerDataManager.getPlayerData(player).setNpcDied(false);
            player.sendMessage("§c§lYour NPC died while you were offline. You have to respawn!.");
            MCSchedulers.getGlobalScheduler().schedule(() ->{
                player.setGameMode(GameMode.SPECTATOR);
                deathRespawnMenu.open(player);
                ItemBuilder.of(Material.BRICK).name("§c§lYou have to respawn!").build();
            }, 1);
        }

        Bukkit.getOnlinePlayers().forEach(player1 -> {
            nameTagService.update(MCPlayer.from(player1));
        });
        clanManager.setHologram(player);
    }

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent event) {
        event.setQuitMessage("§e" + event.getPlayer().getName() + " has left!");

        craftTimerManager.getPlayerCraftTimerList(event.getPlayer()).forEach(craftTimer -> {
            craftTimerManager.craftingFailed(event.getPlayer(), craftTimer);
        });
    }
}
