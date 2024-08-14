package me.orange.anan.player.config;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class PlayerConfig extends YamlConfiguration {
    @ElementType(PlayerConfigElement.class)
    Map<String, PlayerConfigElement> playerElementMap = new HashMap<>();

    public PlayerConfig(Anan plugin) {
        super(plugin.getDataFolder().resolve("player.yml"));
        this.loadAndSave();
    }

    public void addPlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        if(!playerElementMap.containsKey(uuid)) {
            playerElementMap.put(uuid, new PlayerConfigElement());
            this.save();
            this.load();
        }
    }

    public Map<String, PlayerConfigElement> getPlayerElementMap() {
        return playerElementMap;
    }

    public void setPlayerElementMap(Map<String, PlayerConfigElement> playerElementMap) {
        this.playerElementMap = playerElementMap;
    }

    public PlayerConfigElement getPlayerElement(String name) {
        return getPlayerElementMap().get(name);
    }

    public void addPlayerKills(String name) {
        getPlayerElement(name).setKills(getPlayerElement(name).getKills() + 1);
        this.save();
    }

    public void addPlayerDeaths(String name) {
        getPlayerElement(name).setDeaths(getPlayerElement(name).getDeaths() + 1);
        this.save();
    }
}
