package me.orange.anan.craft.building;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.nbt.NBTKey;
import io.fairyproject.bukkit.nbt.NBTModifier;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftTier;
import me.orange.anan.craft.CraftType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class GlassPane implements Craft {
    @Override
    public ItemStack getItemStack() {
        return ItemBuilder.of(XMaterial.GLASS_PANE)
                .name("玻璃片")
                .lore("§f可透過此觀察敵人", "§f能夠抵擋些微傷害")
                .tag("glassPane", "build")
                .build();
    }

    @Override
    public List<ItemStack> getRecipe() {
        return Arrays.asList(
                ItemBuilder.of(XMaterial.GLASS)
                        .amount(10)
                        .tag("glass", "resource")
                        .build()
        );
    }

    @Override
    public int getTime() {
        return 0;
    }

    @Override
    public CraftTier getTier() {
        return CraftTier.COMMON;
    }

    @Override
    public CraftType getType() {
        return CraftType.BUILD;
    }

    @Override
    public XMaterial getMenuIcon() {
        return XMaterial.GLASS_PANE;
    }

    @Override
    public String getID() {
        return NBTModifier.get().getString(getItemStack(), NBTKey.create("build"));
    }

    @Override
    public String getName() {
        return getItemStack().getItemMeta().getDisplayName();
    }

    @Override
    public List<String> getLore() {
        return getItemStack().getItemMeta().getLore();
    }

}
