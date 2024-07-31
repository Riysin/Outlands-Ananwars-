package me.orange.anan.craft;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Craft {
    private ItemStack itemStack;
    private List<ItemStack> recipe = new ArrayList<>();
    private int time = 0;
    private CraftTier tier = CraftTier.COMMON;
    private CraftType type = CraftType.ALL;
    private XMaterial menuIcon = XMaterial.STONE;

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String getName() {
        return getItemStack().getItemMeta().getDisplayName();
    }

    public List<String> getLore() {
        return getItemStack().getItemMeta().getLore();
    }

    public List<ItemStack> getRecipe() {
        return recipe;
    }

    public void setRecipe(List<ItemStack> recipe) {
        this.recipe = recipe;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public CraftTier getTier() {
        return tier;
    }

    public void setTier(CraftTier tier) {
        this.tier = tier;
    }

    public CraftType getType() {
        return type;
    }

    public void setType(CraftType type) {
        this.type = type;
    }

    public XMaterial getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(XMaterial menuIcon) {
        this.menuIcon = menuIcon;
    }
}
