package me.orange.anan.npc;

import io.fairyproject.config.annotation.ConfigurationElement;

@ConfigurationElement
public class LootConfigElement {
    private String id = "";
    private String item = "";
    private int weight = 1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
