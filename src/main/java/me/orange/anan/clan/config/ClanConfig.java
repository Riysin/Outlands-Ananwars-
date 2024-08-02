package me.orange.anan.clan.config;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.player.config.PlayerConfigElement;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class ClanConfig extends YamlConfiguration {

    @ElementType(ClanConfigElement.class)
    Map<String, ClanConfigElement> clanElementMap = new HashMap<>();

    public ClanConfig(Anan plugin) {
        super(plugin.getDataFolder().resolve("clan.yml"));
    }

    public void addClan(String name, Player player) {
        if (!clanElementMap.containsKey(name)) {
            ClanConfigElement element = new ClanConfigElement();
            element.setClanName(name);
            element.setOwner(player.getUniqueId());
            element.addPlayer(player);
            clanElementMap.put(name, element);
            this.save();
            this.load();
        }
    }

    public void removeClan(String name) {
        clanElementMap.remove(name);
        this.save();
        this.load();
    }

    public Map<String, ClanConfigElement> getClanElementMap() {
        return clanElementMap;
    }

    public void setClanElementMap(Map<String, ClanConfigElement> clanElementMap) {
        this.clanElementMap = clanElementMap;
    }
}
