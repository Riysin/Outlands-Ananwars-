package me.orange.anan.clan.config;

import io.fairyproject.config.annotation.ConfigurationElement;

@ConfigurationElement
public class ClanPlayerElement {
    private String name;

    public ClanPlayerElement(){}
    public ClanPlayerElement(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
