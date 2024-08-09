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

    public String getTimeState(World world){
        return isDay.getOrDefault(world, true) ? "白天" : "夜晚";
    }

    @PostInitialize
    public void init(){
        MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            for (World world : Bukkit.getWorlds()) {

            }
        }, 0, 1);
    }
}
