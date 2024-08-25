package me.orange.anan.world;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.mc.scheduler.MCSchedulers;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class TimeManager {
    private Map<World, Boolean> isDay = new HashMap<>();

    public String getTimeState(World world){
        return isDay.getOrDefault(world, true) ? "Day §6✹" : "Night §b☾";
    }

    @PostInitialize
    public void init(){
        MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            for (World world : Bukkit.getWorlds()) {
                long time = world.getTime();
                boolean isDayTime = time <= 14000 || time >= 23000;
                isDay.put(world, isDayTime);
            }
        }, 0, 10);

        MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            for (World world : Bukkit.getWorlds()) {
                for (Player player : world.getPlayers()) {
                    Block block = world.getBlockAt(player.getLocation());
                    if(isDay.get(world) || block.getLightLevel() >= 5){
                        player.removePotionEffect(PotionEffectType.BLINDNESS);
                    }else {
                        PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 999999, 0, false, false);
                        player.addPotionEffect(blind);
                        player.setSprinting(true);
                    }
                }
            }
        }, 0, 1);
    }
}
