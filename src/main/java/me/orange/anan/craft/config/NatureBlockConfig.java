package me.orange.anan.craft.config;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;
import org.bukkit.Material;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@InjectableComponent
public class NatureBlockConfig extends YamlConfiguration {
    @ElementType(NatureBlockElement.class)
    private List<NatureBlockElement> natureBlocks = new ArrayList<>();
    protected NatureBlockConfig(Anan plugin) {
        super(plugin.getDataFolder().resolve("natureBlocks.yml"));
        this.loadAndSave();

        //test
        if(natureBlocks.isEmpty()){
            NatureBlockElement element = new NatureBlockElement();
            Map<String, Integer> drops = new HashMap<>();
            drops.put("stick", 1);
            element.setDrops(drops);
            element.setBlockId(17);

            natureBlocks.add(element);

            this.save();
        }
    }

    public List<NatureBlockElement> getNatureBlocks() {
        return natureBlocks;
    }
}
