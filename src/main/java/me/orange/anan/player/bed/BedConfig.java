package me.orange.anan.player.bed;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@InjectableComponent
public class BedConfig extends YamlConfiguration {
    public BedConfig(Anan plugin) {
        super(plugin.getDataFolder().resolve("beds.yml"));
        this.loadAndSave();
    }

    @ElementType(BedConfigElement.class)
    private Map<String, BedConfigElement> playerBedMap = new HashMap<>();

    public Map<String, BedConfigElement> getPlayerBedMap() {
        return playerBedMap;
    }

    public void setPlayerBedMap(Map<String, BedConfigElement> playerBedMap) {
        this.playerBedMap = playerBedMap;
    }

    public List<BedElement> getBedElements(Player player) {
        return playerBedMap.get(player.getUniqueId().toString()).getBedList();
    }

    public void addBed(Player player, Location location) {
        String uuid = player.getUniqueId().toString();

        if (!playerBedMap.containsKey(uuid)) {
            playerBedMap.put(uuid, new BedConfigElement());
        }
        BedElement element = new BedElement();
        element.setBedName("Bed " + (playerBedMap.get(uuid).getBedList().size() + 1));
        element.setPosition(location);

        playerBedMap.get(uuid).getBedList().add(element);

        this.save();
    }
}
