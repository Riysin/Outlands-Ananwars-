package me.orange.anan.world;

import io.fairyproject.container.InjectableComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;

@InjectableComponent
public class WorldManager {
    public void bukkitCreateWorld(String worldName) {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "正在創建世界(" + worldName + "), 請稍後...");
        WorldCreator wc = new WorldCreator(worldName);
        wc.createWorld();

        Bukkit.broadcastMessage(ChatColor.GREEN + worldName + " 創建完成!");
    }

    public void bukkitRemoveWorld(String worldName){
        Bukkit.broadcastMessage(ChatColor.YELLOW + "正在刪除世界(" + worldName + "), 請稍後...");
    }
}
