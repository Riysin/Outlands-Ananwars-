package me.orange.anan.player.deathloot;

import com.cryptomorin.xseries.messages.Titles;
import io.fairyproject.bukkit.events.player.EntityDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.events.PlayerSaveCanceledEvent;
import me.orange.anan.events.PlayerSaveEvent;
import me.orange.anan.player.PlayerDataManager;
import me.orange.anan.player.bed.RespawnMenu;
import me.orange.anan.player.config.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@InjectableComponent
@RegisterAsListener
public class DeathEventListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final DeathLootManager deathLootManager;
    private final ClanManager clanManager;
    private final RespawnMenu respawnMenu;
    private final DeathBossBar deathBossBar;

    public DeathEventListener(PlayerDataManager playerDataManager, DeathLootManager deathLootManager, ClanManager clanManager, RespawnMenu respawnMenu, DeathBossBar deathBossBar) {
        this.playerDataManager = playerDataManager;
        this.deathLootManager = deathLootManager;
        this.clanManager = clanManager;
        this.respawnMenu = respawnMenu;
        this.deathBossBar = deathBossBar;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        boolean isKnocked = playerDataManager.checkKnocked(player);

        if (!isKnocked) {
            playerDataManager.playerKnocked(player);
            player.sendMessage("§c你被擊倒了");
        } else {
            playerDataManager.playerSaved(player);
            playerDataManager.getPlayerData(player).addDeath();
            playerDataManager.getPlayerData(player).setLastDeathLocation(player.getLocation());
            deathLootManager.addPlayer(player, player.getLocation());

            Player killer = player.getKiller();
            event.setDeathMessage(killer == null ?
                    "§c" + player.getName() + "意外死亡了" :
                    "§c" + player.getName() + "被" + killer.getName() + "擊殺了");
            player.setHealth(20);
            player.setGameMode(GameMode.SPECTATOR);
            respawnMenu.open(player);
            deathBossBar.showBossBar(player);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (playerDataManager.checkKnocked(player) && !player.isOnGround() && from.getY() < to.getY()) {
            player.setVelocity(new Vector(0, 0, 0));
        }

        if (playerDataManager.checkSaving(player) && !from.equals(to)) {
            player.teleport(from);
        }

        deathBossBar.updateBossBar(player);
    }

    @EventHandler
    public void saveKnockedPlayer(PlayerInteractEntityEvent event) {
        Player eventPlayer = event.getPlayer();
        if (!(event.getRightClicked() instanceof Player)) {
            return;
        }
        Player savedPlayer = (Player) ((Player) event.getRightClicked()).getPlayer();
        if (playerDataManager.getPlayerData(eventPlayer).isSaving()) {
            Bukkit.getPluginManager().callEvent(new PlayerSaveCanceledEvent(eventPlayer, savedPlayer));
        } else {
            Bukkit.getPluginManager().callEvent(new PlayerSaveEvent(eventPlayer, savedPlayer));
        }
    }

    @EventHandler
    public void onSave(PlayerSaveEvent event) {
        Player eventPlayer = event.getEventPlayer();
        Player savedPlayer = event.getSavedPlayer();

        if (clanManager.sameClan(eventPlayer, savedPlayer) && playerDataManager.checkKnocked(savedPlayer)) {
            AtomicInteger progressCounter = new AtomicInteger(0);
            playerDataManager.setSavingStats(eventPlayer, savedPlayer, true);

            CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
                if (!playerDataManager.checkSaving(savedPlayer)) {
                    return TaskResponse.failure("canceled");
                }

                double progress = (double) progressCounter.get() / 100;
                String progressBar = createProgressBar(progress);
                Titles.sendTitle(eventPlayer, 0, 20, 10, "", progressBar);
                Titles.sendTitle(savedPlayer, 0, 20, 10, "", progressBar);
                progressCounter.getAndIncrement();

                return TaskResponse.continueTask();
            }, 0, 1, RepeatPredicate.length(Duration.ofSeconds(5))).getFuture();

            future.thenRun(() -> {
                Titles.sendTitle(eventPlayer, 0, 20, 10, "", "§afinished");
                Titles.sendTitle(savedPlayer, 0, 20, 10, "", "§afinished");
                playerDataManager.setSavingStats(eventPlayer, savedPlayer, false);
                playerDataManager.playerSaved(savedPlayer);
            });
        }
    }

    @EventHandler
    public void saveFailed(PlayerSaveCanceledEvent event) {
        Player eventPlayer = event.getEventPlayer();
        Player savedPlayer = event.getSavedPlayer();

        Titles.sendTitle(eventPlayer, 0, 20, 10, "", "§esaving canceled");
        Titles.sendTitle(savedPlayer, 0, 20, 10, "", "§esaving canceled");
        playerDataManager.setSavingStats(eventPlayer, savedPlayer, false);
    }

    @EventHandler
    public void onRightClickArmorStand(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;
            event.setCancelled(true);
            Bukkit.getServer().dispatchCommand(player, "death loot " + armorStand.getUniqueId());
        }
    }

    @EventHandler
    public void onHitArmorStand(EntityDamageByPlayerEvent event) {
        if (event.getEntity() instanceof ArmorStand) {
            event.setCancelled(true);
        }
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
