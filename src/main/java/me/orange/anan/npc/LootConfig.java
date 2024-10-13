package me.orange.anan.npc;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class LootConfig extends YamlConfiguration {
    protected LootConfig(Anan plugin) {
        super(plugin.getDataFolder().resolve("loot.yml"));
        this.loadAndSave();
    }

    @ElementType(LootConfigElement.class)
    private List<LootConfigElement> loots = new ArrayList<>();

    public List<LootConfigElement> getLoots() {
        return loots;
    }
}
