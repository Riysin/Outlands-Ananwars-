package me.orange.anan.clan.config;

import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.config.annotation.ElementType;
import me.orange.anan.player.config.PlayerConfigElement;

import java.util.ArrayList;
import java.util.List;

@ConfigurationElement
public class ClanConfigElement {
    private String clanName = "";
    private String ownerName = "";

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    @ElementType(ClanPlayerElement.class)
    private List<ClanPlayerElement> players = new ArrayList<>();
    private String prefix = "";
    private String suffix = "";

    public List<ClanPlayerElement> getPlayers() {
        return players;
    }

    public void setPlayers(List<ClanPlayerElement> players) {
        this.players = players;
    }

    public void addPlayer(String name) {
        players.add(new ClanPlayerElement(name));
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String name) {
        this.ownerName = name;
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