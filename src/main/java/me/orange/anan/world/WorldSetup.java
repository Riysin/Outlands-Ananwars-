package me.orange.anan.world;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.log.Log;
import me.orange.anan.craft.config.CraftConfig;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;

@InjectableComponent
public class WorldSetup {
    private final WorldManager worldManager;

    public WorldSetup(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @PostInitialize
    public void init() {
        Bukkit.getWorlds().forEach(worldManager::initializeWorld);
    }
}
