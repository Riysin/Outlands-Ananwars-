package me.orange.anan.player.death;

import com.cryptomorin.xseries.messages.Titles;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.events.PlayerRescueEvent;
import me.orange.anan.events.PlayerRevivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@InjectableComponent
@RegisterAsListener
public class RescueEventListener implements Listener {
    private final ClanManager clanManager;
    private final DeathManager deathManager;

    public RescueEventListener(ClanManager clanManager, DeathManager deathManager) {
        this.clanManager = clanManager;
        this.deathManager = deathManager;
    }

    @EventHandler
    public void playerInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            Player rescuer = event.getPlayer();
            Player rescuedPlayer = (Player) event.getRightClicked();

            if (clanManager.sameClan(rescuer, rescuedPlayer) && deathManager.isDown(rescuedPlayer)) {
                if (deathManager.isRescuing(rescuer)) {
                    event.setCancelled(true);
                    deathManager.stopRescueByRescuer(rescuer);
                    return;
                }

                Bukkit.getPluginManager().callEvent(new PlayerRescueEvent(rescuer, rescuedPlayer));
            }
        }
    }

    @EventHandler
    public void onRescue(PlayerRescueEvent event) {
        Player rescuer = event.getRescuer();
        Player rescuedPlayer = event.getRescuedPlayer();
        deathManager.addRescue(rescuedPlayer, rescuer);

        AtomicInteger progressCounter = new AtomicInteger(0);

        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if (!deathManager.isBeingRescued(rescuedPlayer) || !rescuer.isOnline() || !rescuedPlayer.isOnline()) {
                Titles.sendTitle(rescuer, 0, 20, 10, "", "§esaving canceled");
                Titles.sendTitle(rescuedPlayer, 0, 20, 10, "", "§esaving canceled");
                deathManager.stopRescueByRescuer(rescuer);
                rescuer.playSound(rescuer.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
                rescuedPlayer.playSound(rescuedPlayer.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
                return TaskResponse.failure("event is cancelled");
            }

            double progress = (double) progressCounter.get() / 100;
            String progressBar = createProgressBar(progress);
            Titles.sendTitle(rescuer, 0, 20, 10,  progressBar, "§e再按一次右鍵以取消");
            Titles.sendTitle(rescuedPlayer, 0, 20, 10, progressBar, "§e救援中");
            rescuedPlayer.getWorld().playEffect(rescuer.getLocation(), Effect.HEART, 0);
            if ((progressCounter.get() % 20) == 0)
                rescuedPlayer.getWorld().playSound(rescuedPlayer.getLocation(), Sound.NOTE_STICKS, 1, 1);
            progressCounter.getAndIncrement();

            return TaskResponse.continueTask();
        }, 0, 1, RepeatPredicate.length(Duration.ofSeconds(5))).getFuture();

        future.thenRun(() -> {
            Titles.sendTitle(rescuer, 0, 20, 10, "§a救援完成", "");
            Titles.sendTitle(rescuedPlayer, 0, 20, 10, "§a救援完成", "");
            deathManager.stopRescueByRescuer(rescuer);
            Bukkit.getPluginManager().callEvent(new PlayerRevivedEvent(rescuedPlayer));
        });
    }

    private String createProgressBar(double progress) {
        int totalBars = 20;
        int filledBars = (int) (progress * totalBars);
        int emptyBars = totalBars - filledBars;

        StringBuilder progressBar = new StringBuilder();
        progressBar.append("§a");
        for (int i = 0; i < filledBars; i++) {
            progressBar.append("|");
        }
        progressBar.append("§7");
        for (int i = 0; i < emptyBars; i++) {
            progressBar.append("|");
        }

        return progressBar.toString();
    }

    @EventHandler
    public void onPlayerRevived(PlayerRevivedEvent event) {
        Player player = event.getPlayer();

        player.setSneaking(false);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.WITHER);
        player.removePotionEffect(PotionEffectType.WEAKNESS);
        player.getWorld().playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1);
        deathManager.removeDownPlayer(player);
    }
}
