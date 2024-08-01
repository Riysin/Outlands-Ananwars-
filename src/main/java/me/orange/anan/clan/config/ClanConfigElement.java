package me.orange.anan.clan.config;

import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.config.annotation.ElementType;
import me.orange.anan.player.config.PlayerConfigElement;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConfigurationElement
public class ClanConfigElement {
    private String clanName = "";
    private String owner = "";
    private List<String> players = new ArrayList<>();
    private String prefix = "";
    private String suffix = "";

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public List<UUID> getPlayers() {
        List<UUID> uuids = new ArrayList<>();
        players.forEach(player -> uuids.add(UUID.fromString(player)));
        return uuids;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player.getUniqueId().toString());
    }
    public void addPlayer(UUID uuid) {
        players.add(uuid.toString());
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId().toString());
    }

    public UUID getOwner() {
        return UUID.fromString(owner);
    }

    public void setOwner(UUID uuid) {
        this.owner = uuid.toString();
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}