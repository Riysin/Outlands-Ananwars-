package me.orange.anan.world;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.log.Log;
import me.orange.anan.craft.config.CraftConfig;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;

@InjectableComponent
public class WorldSetup {
    @PostInitialize
    public void init() {
        Bukkit.getWorlds().forEach(world -> {
            world.setGameRuleValue("keepInventory", "true");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setGameRuleValue("doDaylightCycle", "true");
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("doTileDrops", "false");
            world.setTime(7000);
            world.setDifficulty(Difficulty.EASY);
            world.setStorm(false);
            world.setWeatherDuration(999999);
            Log.info(world.getName() + " keepInventory on");
        });
    }
}
