package me.orange.anan.player.deathloot;

import com.cryptomorin.xseries.messages.Titles;
import io.fairyproject.bukkit.events.player.EntityDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.events.PlayerSaveCanceledEvent;
import me.orange.anan.events.PlayerSaveEvent;
import me.orange.anan.player.PlayerDataManager;
import me.orange.anan.player.config.PlayerConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@InjectableComponent
@RegisterAsListener
public class DeathEventListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final PlayerConfig playerConfig;
    private final DeathLootManager deathLootManager;
    private final ClanManager clanManager;

    public DeathEventListener(PlayerDataManager playerDataManager, PlayerConfig playerConfig, DeathLootManager deathLootManager, ClanManager clanManager) {
        this.playerDataManager = playerDataManager;
        this.playerConfig = playerConfig;
        this.deathLootManager = deathLootManager;
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onRightClickArmorStand(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;
            event.setCancelled(true);
            Bukkit.getServer().dispatchCommand(player, "death loot " + String.valueOf(armorStand.getUniqueId()));
        }
    }

    @EventHandler
    public void onHitArmorStand(EntityDamageByPlayerEvent event) {
        if (event.getEntity() instanceof ArmorStand) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        if (!playerDataManager.checkKnocked(player)) {
            playerDataManager.playerKnocked(player);
            player.sendMessage("§c你被擊倒了");
        } else {
            playerDataManager.playerSaved(player);
            playerConfig.addPlayerDeaths(player.getName());
            deathLootManager.addPlayer(player, player.getLocation());
            if (event.getEntity().getKiller() == null) {
                event.setDeathMessage("§c" + player.getName() + "意外死亡了");
                return;
            }
            event.setDeathMessage("§c" + player.getName() + "被" + event.getEntity().getKiller().getName() + "擊殺了");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if (playerDataManager.checkKnocked(player)) {
            if (event.getFrom().getY() < event.getTo().getY() && !player.isOnGround()) {
                player.setVelocity(new Vector(0, 0, 0));
            }
        }
        if (playerDataManager.checkSaving(player)) {
            if (from != to) {
                player.teleport(from);
            }
        }
    }

    @EventHandler
    public void saveKnockedPlayer(PlayerInteractEntityEvent event) {
        Player eventPlayer = event.getPlayer();
        if (!(event.getRightClicked() instanceof Player)) {
            return;
        }
        Player savedPlayer = ((Player) event.getRightClicked()).getPlayer();
        if (playerDataManager.getPlayerData(eventPlayer).isSaving()) {
            Bukkit.getPluginManager().callEvent(new PlayerSaveCanceledEvent(eventPlayer, savedPlayer));
            return;
        }
        Bukkit.getPluginManager().callEvent(new PlayerSaveEvent(eventPlayer, savedPlayer));
    }

    @EventHandler
    public void onSave(PlayerSaveEvent event) {
        Player eventPlayer = event.getEventPlayer();
        Player savedPlayer = event.getSavedPlayer();

        if (clanManager.sameClan(eventPlayer, savedPlayer))
            if (playerDataManager.checkKnocked(savedPlayer)) {
                playerDataManager.setSavingStats(eventPlayer, savedPlayer, true);
                AtomicInteger i = new AtomicInteger(0);

                CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
                    if (!playerDataManager.checkSaving(savedPlayer)) {
                        return TaskResponse.failure("canceled");
                    }

                    double progress = (double) i.get() / 5;
                    String progressBar = createProgressBar(progress);
                    Titles.sendTitle(eventPlayer, 0, 20, 10, "", progressBar);
                    Titles.sendTitle(savedPlayer, 0, 20, 10, "", progressBar);
                    i.getAndIncrement();

                    return TaskResponse.continueTask();
                }, 0, 1, RepeatPredicate.length(Duration.ofSeconds(5))).getFuture();
                future.thenRun(() -> {
                    eventPlayer.sendTitle("", "§afinished");
                    savedPlayer.sendTitle("", "§afinished");
                    playerDataManager.setSavingStats(eventPlayer, savedPlayer, false);
                    playerDataManager.playerSaved(savedPlayer);
                });
            }
    }


    @EventHandler
    public void saveFailed(PlayerSaveCanceledEvent event) {
        Player eventPlayer = event.getEventPlayer();
        Player savedPlayer = event.getSavedPlayer();
        eventPlayer.sendTitle("", "§esaving canceled");
        savedPlayer.sendTitle("", "§esaving canceled");
        playerDataManager.setSavingStats(eventPlayer, savedPlayer, false);
    }

    private String createProgressBar(double progress) {
        int totalBars = 20; // 进度条总长度
        int filledBars = (int) (progress * totalBars);
        int emptyBars = totalBars - filledBars;

        StringBuilder progressBar = new StringBuilder();
        progressBar.append("§a"); // 已填充部分的颜色
        for (int i = 0; i < filledBars; i++) {
            progressBar.append("|");
        }
        progressBar.append("§7"); // 未填充部分的颜色
        for (int i = 0; i < emptyBars; i++) {
            progressBar.append("|");
        }

        return progressBar.toString();
    }
}
