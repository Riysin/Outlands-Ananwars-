package me.orange.anan.craft.usage;

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

public class CraftingTable implements Craft {
    @Override
    public ItemStack getItemStack() {
        return ItemBuilder.of(XMaterial.CRAFTING_TABLE)
                .name("合成台")
                .lore("§f最初階的合成台", "§f可用於合成中階物品")
                .tag("craftingTable", "usage")
                .build();
    }

    @Override
    public List<ItemStack> getRecipe() {
        return Arrays.asList(
                ItemBuilder.of(XMaterial.STICK)
                        .amount(4)
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
        return XMaterial.matchXMaterial(getItemStack().getType());
    }

    @Override
    public String getID() {
        return NBTModifier.get().getString(getItemStack(), NBTKey.create("usage"));
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

