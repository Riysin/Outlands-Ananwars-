package me.orange.anan.craft;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Craft {
    ItemStack getItemStack();
    List<ItemStack> getRecipe();
    int getTime();
    CraftTier getTier();
    CraftType getType();
    XMaterial getMenuIcon();
    String getID();
    String getName();
    List<String> getLore();






}
