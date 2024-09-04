package me.orange.anan.world;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.mc.scheduler.MCSchedulers;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class TimeManager {
    private Map<World, Boolean> isDay = new HashMap<>();
    private static final double HIDE_DISTANCE = 10.0;

    public String getTimeState(World world) {
        return isDay.getOrDefault(world, true) ? "Day §6✹" : "Night §b☾";
    }

    @PostInitialize
    public void init() {
        MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            for (World world : Bukkit.getWorlds()) {
                long time = world.getTime();
                boolean isDayTime = time <= 14000 || time >= 23000;
                isDay.put(world, isDayTime);
            }
        }, 0, 10);

        MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                    if(isDay.get(player.getWorld())) continue;
                    if (player.equals(otherPlayer)) continue;

                    double distance = player.getLocation().distance(otherPlayer.getLocation());
                    if (distance > HIDE_DISTANCE && otherPlayer.getWorld().getBlockAt(otherPlayer.getLocation()).getLightLevel() <= 5) {
                        player.hidePlayer(otherPlayer);
                    } else {
                        player.showPlayer(otherPlayer);
                    }
                }
            }
        }, 0, 1);
    }
}
