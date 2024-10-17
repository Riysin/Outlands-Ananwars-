package me.orange.anan.util;

import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.craft.CraftType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemLoreBuilder {
    private ItemStack itemStack;
    private CraftManager craftManager;
    private Craft craft;
    private CraftType craftType;
    private int damage;
    private List<String> description;
    private Map<Enchantment, Integer> enchantments;
    private List<String> recipeLines = new ArrayList<>();

    // Private constructor to ensure `of` is the entry point
    private ItemLoreBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static ItemLoreBuilder of(ItemStack itemStack) {
        return new ItemLoreBuilder(itemStack);
    }

    public ItemLoreBuilder setCraft(CraftManager craftManager, Craft craft) {
        this.craftManager = craftManager;
        this.craft = craft;
        return this;
    }

    // Optional: Set craft type
    public ItemLoreBuilder craftType() {
        this.craftType = craft.getType();
        return this;
    }

    // Optional: Set damage
    public ItemLoreBuilder damage() {
        this.damage = craftManager.getDamage(itemStack);
        return this;
    }

    // Optional: Set the description
    public ItemLoreBuilder description() {
        this.description = craft.getLore();
        return this;
    }

    // Optional: Set enchantments
    public ItemLoreBuilder enchantments() {
        if (!itemStack.getEnchantments().isEmpty()) {
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

    public ItemLoreBuilder recipe(Player player) {
        boolean canCraft = craftManager.canCraftItem(player, craft);

        recipeLines.add("§e所需材料:");

        for (ItemStack itemStack : craftManager.getRecipeList(craft.getRecipe(), player)) {
            String itemName = itemStack.getItemMeta().getDisplayName();
            int playerAmount = craftManager.getPlayerItemAmount(player, itemStack);
            boolean hasEnough = craftManager.hasEnough(player, itemStack);
            recipeLines.add((hasEnough ? "§a   " : "§c   ") + itemName + " §7(" + playerAmount + "/" + itemStack.getAmount() + ")");
        }

        recipeLines.add("");
        recipeLines.add("§8需要花費" + craft.getTime() + "秒製作");
        recipeLines.add((canCraft ? "§e點擊合成此物品" : "§c材料不足"));

        return this;
    }

    // Build method to return a list of lore strings
    public List<String> build() {
        List<String> lore = new ArrayList<>();

        if (craftType != null) {
            lore.add("§8" + craftType);
        }

        if (damage >= 1 && (craftType == CraftType.TOOL || craftType == CraftType.COMBAT)) {
            lore.add("");
            lore.add("§7Damage: §c" + damage);
        }

        if (description != null) {
            lore.add("");
            lore.addAll(description);
        }

        if (enchantments != null) {
            lore.add("");
            ItemMeta meta = itemStack.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemStack.setItemMeta(meta);
            enchantments.forEach((enchantment, level) -> lore.add("§9" + getEnchantmentDisplayName(enchantment) + " " + level));
        }

        if (!recipeLines.isEmpty()) {
            lore.add("");
            lore.addAll(recipeLines);
        }

        return lore;
    }

    public ItemStack applyLore() {
        return ItemBuilder.of(itemStack)
                .clearLore()
                .lore(build())
                .build();
    }

    private String getEnchantmentDisplayName(Enchantment enchantment) {
        Map<Enchantment, String> enchantmentNames = new HashMap<>();
        enchantmentNames.put(Enchantment.DURABILITY, "Unbreaking");
        enchantmentNames.put(Enchantment.DAMAGE_ALL, "Sharpness");
        enchantmentNames.put(Enchantment.FIRE_ASPECT, "Fire Aspect");
        enchantmentNames.put(Enchantment.ARROW_DAMAGE, "Power");
        enchantmentNames.put(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection");
        enchantmentNames.put(Enchantment.PROTECTION_FIRE, "Fire Protection");
        enchantmentNames.put(Enchantment.PROTECTION_EXPLOSIONS, "Blast Protection");
        enchantmentNames.put(Enchantment.PROTECTION_PROJECTILE, "Projectile Protection");

        // Default to the Bukkit key if the enchantment is not found in the map
        return enchantmentNames.getOrDefault(enchantment, enchantment.getName());
    }
}