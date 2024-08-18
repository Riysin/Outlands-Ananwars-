package me.orange.anan.clan.config;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;

import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class ClanConfig extends YamlConfiguration {

    @ElementType(ClanConfigElement.class)
    private Map<String, ClanConfigElement> clanElementMap = new HashMap<>();

    public ClanConfig(Anan plugin) {
        super(plugin.getDataFolder().resolve("clan.yml"));
        this.loadAndSave();
    }

    public Map<String, ClanConfigElement> getClanElementMap() {
        return clanElementMap;
    }

    public void setClanElementMap(Map<String, ClanConfigElement> clanElementMap) {
        this.clanElementMap = clanElementMap;
    }
}
