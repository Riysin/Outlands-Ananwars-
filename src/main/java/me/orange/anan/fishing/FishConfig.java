package me.orange.anan.fishing;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;
import me.orange.anan.craft.CraftManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@InjectableComponent
public class FishConfig extends YamlConfiguration {
    private final CraftManager craftManager;

    @ElementType(FishConfigElement.class)
    private Map<String, FishConfigElement> fishMap = new HashMap<>();

    protected FishConfig(Anan plugin, CraftManager craftManager) {
        super(plugin.getDataFolder().resolve("fish.yml"));
        this.craftManager = craftManager;

        loadFishes();

        this.loadAndSave();
    }

    private void loadFishes() {
        List<String> fishList = new ArrayList<>();
        fishList.add("cod");
        fishList.add("salmon");
        fishList.add("pufferfish");
        fishList.add("tropicalFish");

        for (String fish : fishList) {
            if (fishMap.containsKey(fish) || !craftManager.getCrafts().containsKey(fish)) {
                continue;
            }
            fishMap.put(fish, new FishConfigElement());
        }
        this.save();
    }

    public Map<String, FishConfigElement> getFishMap() {
        return fishMap;
    }

    public void setFishMap(Map<String, FishConfigElement> fishMap) {
        this.fishMap = fishMap;
    }
}
