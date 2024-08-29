package me.orange.anan.player.config;

import io.fairyproject.config.annotation.ConfigurationElement;

@ConfigurationElement
public class JobElement {
    private int level = 0;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
