package me.orange.anan.player.config;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@InjectableComponent
public class PlayerConfig extends YamlConfiguration {
    @ElementType(PlayerConfigElement.class)
    Map<String, PlayerConfigElement> playerElementMap = new HashMap<>();

    public PlayerConfig(Anan plugin) {
        super(plugin.getDataFolder().resolve("player.yml"));
        this.loadAndSave();
    }

    public Map<String, PlayerConfigElement> getPlayerElementMap() {
        return playerElementMap;
    }

    public void setPlayerElementMap(Map<String, PlayerConfigElement> playerElementMap) {
        this.playerElementMap = playerElementMap;
    }

    public PlayerConfigElement getPlayerConfigElement(UUID id) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(id);
        String uuid = player.getUniqueId().toString();
        if(!playerElementMap.containsKey(uuid)) {
            playerElementMap.put(uuid, new PlayerConfigElement());
        }
        playerElementMap.get(uuid).setName(player.getName());
        this.save();

        return playerElementMap.get(uuid);
    }
}
