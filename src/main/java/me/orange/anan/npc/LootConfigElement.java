package me.orange.anan.npc;

import io.fairyproject.config.annotation.ConfigurationElement;

@ConfigurationElement
public class LootConfigElement {
    private String id = "";
    private int amount = 1;
    private int weight = 1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
