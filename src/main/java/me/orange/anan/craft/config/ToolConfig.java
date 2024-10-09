package me.orange.anan.craft.config;

import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.craft.CraftType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class ToolConfig extends YamlConfiguration {
    private Map<String, Integer> toolMap = new HashMap<>();

    protected ToolConfig(Anan plugin) {
        super(plugin.getDataFolder().resolve("tools.yml"));
        this.loadAndSave();
    }

    public Map<String, Integer> getToolMap(){
        return this.toolMap;
    }

    public void setToolMap(Map<String, Integer> toolMap){
        this.toolMap = toolMap;
    }
}
