package me.orange.anan.npc;

import org.bukkit.inventory.ItemStack;

public class Loot {
    private ItemStack item;
    private int weight;

    public Loot() {
    }

    public Loot(ItemStack item, int weight) {
        this.item = item;
        this.weight = weight;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
