package me.orange.anan.world;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.log.Log;
import org.bukkit.*;

import java.io.File;

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

        WorldBorder worldBorder = world.getWorldBorder();

        worldBorder.setCenter(0, 0);
        worldBorder.setSize(3000);
        worldBorder.setWarningDistance(10);
        Log.info(world.getName() + " keepInventory on");
    }

    public void bukkitRemoveWorld(String worldName){
        Bukkit.broadcastMessage(ChatColor.YELLOW + "正在刪除世界(" + worldName + "), 請稍後...");
        World world = Bukkit.getWorld(worldName);

        if (world != null) {
            boolean unloaded = Bukkit.unloadWorld(world, false); // 'false' = don't save changes
            if (!unloaded) {
                Bukkit.broadcastMessage("Failed to unload world " + worldName);
                return;
            }
        }

        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        if (worldFolder.exists()) {
            deleteWorldFolder(worldFolder);
            Bukkit.broadcastMessage(ChatColor.GREEN + worldName + " 刪除完成!");
        } else {
            Bukkit.broadcastMessage("World folder not found: " + worldFolder.getPath());
        }
    }

    private void deleteWorldFolder(File path) {
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteWorldFolder(file); // Recursively delete subfolders
                } else {
                    file.delete(); // Delete file
                }
            }
        }
        path.delete(); // Finally, delete the folder itself
    }
}
