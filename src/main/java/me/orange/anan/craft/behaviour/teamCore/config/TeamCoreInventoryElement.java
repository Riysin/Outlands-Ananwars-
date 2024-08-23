package me.orange.anan.craft.behaviour.teamCore.config;

import io.fairyproject.config.annotation.ConfigurationElement;

@ConfigurationElement
public class TeamCoreInventoryElement {
    private int slot = 0;
    private String id = "";
    private int amount = 0;

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

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
}
