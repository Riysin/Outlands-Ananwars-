package me.orange.anan.player.config;

import io.fairyproject.config.annotation.ConfigurationElement;

@ConfigurationElement
public class PlayerConfigElement {
    private int kills;
    private int deaths;

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

}
