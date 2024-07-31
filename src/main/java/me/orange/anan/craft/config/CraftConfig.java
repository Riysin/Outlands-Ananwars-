package me.orange.anan.craft.config;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;
import me.orange.anan.craft.CraftType;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class CraftConfig extends YamlConfiguration {
    @ElementType(CraftConfigElement.class)
    Map<String, CraftConfigElement> craftTypes = new HashMap<>();

    public CraftConfig(Anan plugin) {
        super(plugin.getDataFolder().resolve("crafts.yml"));
        this.loadAndSave();

        //load craft type to config
        for (CraftType type : CraftType.values()) {
            addCraftType(type);
        }
    }

    public Map<String, CraftConfigElement> getCraftTypes() {
        return craftTypes;
    }

    public void addCraftType(CraftType type) {
        if(!craftTypes.containsKey(type.name())) {
            craftTypes.put(type.name(), new CraftConfigElement());
            this.save();
            this.load();
        }
    }
}
