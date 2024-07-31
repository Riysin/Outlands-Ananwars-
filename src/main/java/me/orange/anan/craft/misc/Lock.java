package me.orange.anan.craft.misc;

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

public class Lock implements Craft {
    @Override
    public ItemStack getItemStack() {
        return ItemBuilder.of(XMaterial.TRIPWIRE_HOOK)
                .name("鎖")
                .lore("§f可將箱子上鎖","§f對箱子按右鍵使用")
                .tag("lock", "misc")
                .build();
    }

    @Override
    public List<ItemStack> getRecipe() {
        return Arrays.asList(
                ItemBuilder.of(XMaterial.IRON_INGOT)
                        .amount(10)
                        .tag("ironIngot", "resource")
                        .build()
        );
    }

    @Override
    public int getTime() {
        return 30;
    }

    @Override
    public CraftTier getTier() {
        return CraftTier.COMMON;
    }

    @Override
    public CraftType getType() {
        return CraftType.USAGE;
    }

    @Override
    public XMaterial getMenuIcon() {
        return XMaterial.matchXMaterial(getItemStack().getType());
    }

    @Override
    public String getID() {
        return NBTModifier.get().getString(getItemStack(), NBTKey.create("misc"));
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

