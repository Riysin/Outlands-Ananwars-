package me.orange.anan.util;

import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.orange.anan.craft.CraftType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemLoreBuilder {
    private final ItemStack itemStack;
    private CraftType craftType;
    private int damage;
    private List<String> description;
    private Map<Enchantment, Integer> enchantments;

    // Private constructor to ensure `of` is the entry point
    private ItemLoreBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static ItemLoreBuilder of(ItemStack itemStack) {
        return new ItemLoreBuilder(itemStack);
    }

    // Optional: Set craft type
    public ItemLoreBuilder craftType(CraftType craftType) {
        this.craftType = craftType;
        return this;
    }

    // Optional: Set damage
    public ItemLoreBuilder damage(int damage) {
        this.damage = damage;
        return this;
    }

    // Optional: Set the description
    public ItemLoreBuilder description(List<String> description) {
        this.description = description;
        return this;
    }

    // Optional: Set enchantments
    public ItemLoreBuilder enchantments() {
        if(!itemStack.getEnchantments().isEmpty()){
            this.enchantments = itemStack.getEnchantments();
        }

        if (itemStack.isSimilar(new ItemStack(Material.ENCHANTED_BOOK))) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            if (meta.hasStoredEnchants()) {
                this.enchantments = meta.getStoredEnchants();
            }
        }
        return this;
    }

    // Build method to return a list of lore strings
    public List<String> build() {
        List<String> lore = new ArrayList<>();

        if (craftType != null) {
            lore.add("§8" + craftType);
            lore.add("");
        }

        if (damage >= 1) {
            lore.add("§7Damage: §c" + damage);
            lore.add("");
        }

        if (description != null) {
            lore.addAll(description);
            lore.add("");
        }

        if (enchantments != null) {
            enchantments.forEach((enchantment, level) -> lore.add("§9" + enchantment.getName() + " " + level));
        }

        return lore;
    }

    public ItemStack applyLore() {
        ItemBuilder itemBuilder = ItemBuilder.of(itemStack);
        itemBuilder.clearLore();
        itemBuilder.lore(build());
        return itemBuilder.build();
    }
}