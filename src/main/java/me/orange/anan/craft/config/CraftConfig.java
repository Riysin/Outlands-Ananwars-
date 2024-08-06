package me.orange.anan.craft.config;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;
import me.orange.anan.craft.CraftType;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@InjectableComponent
public class CraftConfig extends YamlConfiguration {
    @ElementType(CraftConfigElement.class)
    List<CraftConfigElement> craftTypes = new ArrayList<>();

    public CraftConfig(Anan plugin) {
        super(plugin.getDataFolder().resolve("crafts.yml"));
        this.loadAndSave();

        Set<CraftType> types = craftTypes.stream()
                .map(CraftConfigElement::getCraftType)
                .collect(Collectors.toSet());

        for (CraftType value : CraftType.values()) {
            types.add(value); // 仅添加到types，不再需要重复遍历
            CraftConfigElement configElement = new CraftConfigElement();
            configElement.setCraftType(value);
            craftTypes.add(configElement);
        }

        craftTypes.get(0).getCrafts().add(new CraftElement());
        this.save();
    }

    public List<CraftConfigElement> getCraftTypes() {
        return craftTypes;
    }
}
