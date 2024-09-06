package me.orange.anan.player.death;

import io.fairyproject.bukkit.events.player.EntityDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.events.PlayerDeadEvent;
import me.orange.anan.events.PlayerDownEvent;
import me.orange.anan.events.PlayerRevivedEvent;
import me.orange.anan.player.PlayerDataManager;
import me.orange.anan.player.death.deathloot.DeathLootManager;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

@InjectableComponent
@RegisterAsListener
public class DeathEventListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final DeathLootManager deathLootManager;
    private final DeathRespawnMenu deathRespawnMenu;
    private final DeathBossBar deathBossBar;
    private final DeathManager deathManager;

    public DeathEventListener(PlayerDataManager playerDataManager, DeathLootManager deathLootManager, DeathRespawnMenu deathRespawnMenu, DeathBossBar deathBossBar, DeathManager deathManager) {
        this.playerDataManager = playerDataManager;
        this.deathLootManager = deathLootManager;
        this.deathRespawnMenu = deathRespawnMenu;
        this.deathBossBar = deathBossBar;
        this.deathManager = deathManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (player.hasMetadata("NPC")) {
            return;
        }

        if (!deathManager.isDown(player)) {
            event.setDeathMessage(null);
            Bukkit.getPluginManager().callEvent(new PlayerDownEvent(player));
            return;
        }

        Bukkit.getPluginManager().callEvent(new PlayerDeadEvent(player));
    }

    @EventHandler
    public void playerDown(PlayerDownEvent event) {
        Player player = event.getPlayer();

        player.setHealth(10);
        player.setSneaking(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 5, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 250));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 999999, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 999999, 250));
        player.getWorld().spigot().playEffect(player.getLocation().clone().add(0, 1, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK.getId(), 0, 0.25f, 0.25f, 0.25f, 0, 30, 32);
        player.sendTitle("§c你被擊倒了", "§e請等待隊友救援");
        deathManager.addDownPlayer(player);
    }

    @EventHandler
    public void playerDead(PlayerDeadEvent event) {
        Player player = event.getEntity();

        Player killer = player.getKiller();
        event.setDeathMessage(killer == null ?
                "§c" + player.getName() + "意外死亡了" :
                "§c" + player.getName() + "被" + killer.getName() + "擊殺了");

        deathManager.removeDownPlayer(player);
        deathManager.stopRescueByRescued(player);
        playerDataManager.getPlayerData(player).addDeath();
        playerDataManager.getPlayerData(player).setLastDeathLocation(player.getLocation());
        deathLootManager.addPlayer(player, player.getLocation());

        //Revive player and show respawn menu
        Bukkit.getPluginManager().callEvent(new PlayerRevivedEvent(player));
        player.setHealth(20);
        player.setGameMode(GameMode.SPECTATOR);
        deathRespawnMenu.open(player);
        deathBossBar.showBossBar(player);

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (deathManager.isDown(player)  && from.getY() < to.getY()) {
            player.setVelocity(new Vector(0, 0, 0));
        }

        if ((deathManager.isBeingRescued(player) || deathManager.isRescuing(player)) && !from.equals(to)) {
            player.teleport(from);
        }

        deathBossBar.updateBossBar(player);
    }


    @EventHandler
    public void onRightClickArmorStand(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (entity instanceof ArmorStand) {
            event.setCancelled(true);
            ArmorStand armorStand = (ArmorStand) entity;
            Bukkit.getServer().dispatchCommand(player, "death loot " + armorStand.getUniqueId());
        }
    }

    @EventHandler
    public void onHitArmorStand(EntityDamageByPlayerEvent event) {
        if (event.getEntity() instanceof ArmorStand) {
            event.setCancelled(true);
        }
    }
}
