package me.orange.anan.player;

import com.cryptomorin.xseries.XMaterial;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleSubtitle;
import io.fairyproject.bukkit.events.player.EntityDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.mc.tablist.util.Skin;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.player.config.PlayerConfig;
import me.orange.anan.player.deathloot.DeathLootManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@InjectableComponent
@RegisterAsListener
public class PlayerEventListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final ClanManager clanManager;
    private final DeathLootManager deathLootManager;
    private final NameTagService nameTagService;
    private final PlayerConfig playerConfig;

    public PlayerEventListener(PlayerDataManager playerDataManager, ClanManager clanManager, DeathLootManager deathLootManager, NameTagService nameTagService, PlayerConfig playerConfig) {
        this.playerDataManager = playerDataManager;
        this.clanManager = clanManager;
        this.deathLootManager = deathLootManager;
        this.nameTagService = nameTagService;
        this.playerConfig = playerConfig;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerDataManager.setUpPlayer(event);
        clanManager.setUpClan();

        nameTagService.update(MCPlayer.from(event.getPlayer()));
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
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        if (!playerDataManager.checkKnocked(player)) {
            playerDataManager.playerKnocked(player);
            event.setDeathMessage("§c你已倒地");
        } else {
            playerDataManager.playerSaved(player);
            playerConfig.addPlayerDeaths(player.getName());
            deathLootManager.addPlayer(player, player.getLocation());
            event.setDeathMessage("§c你被擊殺了");
        }
    }

    @EventHandler
    public void saveKnockedPlayer(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            Player eventPlayer = event.getPlayer();
            Player savedPlayer = ((Player) event.getRightClicked()).getPlayer();
            if (clanManager.sameClan(eventPlayer, savedPlayer))
                if (playerDataManager.checkKnocked(savedPlayer)) {
                    playerDataManager.setSavingStats(eventPlayer, savedPlayer, true);

                    CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
                        eventPlayer.sendTitle("", "§eyou are saving a player");
                        savedPlayer.sendTitle("", "§eyou are being saved");
                    }, 0, 20, RepeatPredicate.length(Duration.ofSeconds(5))).getFuture();
                    future.thenRun(() -> {
                        eventPlayer.sendTitle("", "§afinished");
                        savedPlayer.sendTitle("", "§afinished");
                        playerDataManager.setSavingStats(eventPlayer, savedPlayer, false);
                        playerDataManager.playerSaved(savedPlayer);
                    });
                }
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
    public void onHitArmorStand(EntityDamageByPlayerEvent event){
        if (event.getEntity() instanceof ArmorStand){
            event.setCancelled(true);
        }
    }
}
