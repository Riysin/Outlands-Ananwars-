package me.orange.anan.craft;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Craft {
    String getID();
    String getName();
    int getTime();
    CraftTier getTier();
    CraftType getType();
    List<ItemStack> getRecipe();
    XMaterial getMenuIcon();
    ItemStack getItemStack();
}
