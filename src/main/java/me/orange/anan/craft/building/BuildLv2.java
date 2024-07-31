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

public class BuildLv2 implements Craft {
    @Override
    public ItemStack getItemStack() {
        return ItemBuilder.of(XMaterial.INFESTED_COBBLESTONE)
                .name("木製建材")
                .lore("§f由木板拼接而成", "§f適合打造各種結構")
                .tag("buildLv2", "build")
                .build();
    }

    @Override
    public List<ItemStack> getRecipe() {
        return Arrays.asList(
                ItemBuilder.of(XMaterial.STICK)
                        .amount(16)
                        .tag("stick", "resource")
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
        return XMaterial.INFESTED_COBBLESTONE;
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
