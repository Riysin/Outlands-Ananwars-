package me.orange.anan.blocks.config;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class BlockConfig extends YamlConfiguration {
    public BlockConfig(Anan plugin) {
        super(plugin.getDataFolder().resolve("block.yml"));
        this.loadAndSave();
    }

    @ElementType(BlockConfigElement.class)
    List<BlockConfigElement> blockData = new ArrayList<>();

    public List<BlockConfigElement> getBlockData() {
        return blockData;
    }

    public void setBlockData(List<BlockConfigElement> blockData) {
        this.blockData = blockData;
    }
}
