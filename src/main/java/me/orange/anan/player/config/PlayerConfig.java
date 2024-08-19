package me.orange.anan.player.config;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void addPlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        if(!playerElementMap.containsKey(uuid)) {
            playerElementMap.put(uuid, new PlayerConfigElement());
            this.save();
        }
    }

    public void addBed(Player player, Location location) {
        String uuid = player.getUniqueId().toString();

        if (!playerElementMap.containsKey(uuid)) {
            playerElementMap.put(uuid, new PlayerConfigElement());
        }
        BedElement element = new BedElement();
        element.setBedName("Bed " + (playerElementMap.get(uuid).getBedList().size() + 1));
        element.setPosition(location);

        playerElementMap.get(uuid).getBedList().add(element);

        this.save();
    }
}
