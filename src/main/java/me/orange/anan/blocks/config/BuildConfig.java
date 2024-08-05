package me.orange.anan.blocks.config;

import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.craft.CraftType;

import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class BuildConfig extends YamlConfiguration {
    private Map<String, Integer> buildBlocks = new HashMap<>();

    protected BuildConfig(Anan plugin, CraftManager craftManager) {
        super(plugin.getDataFolder().resolve("buildBlocks.yml"));
        this.loadAndSave();

        craftManager.loadConfigFile();
        craftManager.getCrafts().forEach((s, craft) -> {
            if(craft.getType() == CraftType.BUILD && !buildBlocks.containsKey(s))
                buildBlocks.put(s, 10);
        });
        this.save();
    }

    public Map<String, Integer> getBuildBlocks(){
        return this.buildBlocks;
    }
}
