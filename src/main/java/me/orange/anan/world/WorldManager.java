package me.orange.anan.world;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.log.Log;
import org.bukkit.*;

@InjectableComponent
public class WorldManager {
    public void bukkitCreateWorld(String worldName) {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "正在創建世界(" + worldName + "), 請稍後...");
        WorldCreator wc = new WorldCreator(worldName);
        wc.createWorld();
        initializeWorld(Bukkit.getWorld(worldName));

        Bukkit.broadcastMessage(ChatColor.GREEN + worldName + " 創建完成!");
    }

    public void initializeWorld(World world){
        world.setGameRuleValue("keepInventory", "true");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doDaylightCycle", "true");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("doTileDrops", "false");
        world.setGameRuleValue("doEntityDrops", "false");
        world.setGameRuleValue("doMobLoot", "false");
        world.setTime(7000);
        world.setDifficulty(Difficulty.EASY);
        world.setStorm(false);
        world.setWeatherDuration(999999);
        Log.info(world.getName() + " keepInventory on");
    }

    public void bukkitRemoveWorld(String worldName){
        Bukkit.broadcastMessage(ChatColor.YELLOW + "正在刪除世界(" + worldName + "), 請稍後...");
    }
}
