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
    private final CraftManager craftManager;
    private Map<String, Integer> toolMap = new HashMap<>();

    protected ToolConfig(Anan plugin, CraftManager craftManager) {
        super(plugin.getDataFolder().resolve("tools.yml"));
        this.craftManager = craftManager;
        this.loadAndSave();

        craftManager.loadConfigFile();
        craftManager.getCrafts().forEach((s, craft) -> {
            if(craft.getType() == CraftType.TOOL && !toolMap.containsKey(s))
                toolMap.put(s, 1);
        });
        this.save();
    }

    public Map<String, Integer> getToolMap(){
        return this.toolMap;
    }

    public int getToolDamage(ItemStack toolItem){
        if(toolItem == null || craftManager.getCraft(toolItem) == null){
            return 1;
        }
        return toolMap.getOrDefault(craftManager.getCraft(toolItem).getID(), 1);
    }
}
