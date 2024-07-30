package me.orange.anan.craft.tool;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftTier;
import me.orange.anan.craft.CraftType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class WoodenPickaxe implements Craft {
    @Override
    public String getID() {
        return "woodenPickaxe";
    }

    @Override
    public String getName() {
        return "木鎬";
    }

    @Override
    public int getTime() {
        return 10;
    }

    @Override
    public CraftTier getTier() {
        return CraftTier.COMMON;
    }

    @Override
    public CraftType getType() {
        return CraftType.TOOL;
    }

    @Override
    public List<ItemStack> getRecipe() {
        return Arrays.asList(
                ItemBuilder.of(XMaterial.STICK)
                        .amount(2)
                        .tag("stick", "resource")
                        .build(),
                ItemBuilder.of(XMaterial.OAK_PLANKS)
                        .amount(3)
                        .tag("kurWood", "resource")
                        .build()
        );
    }

    @Override
    public XMaterial getMenuIcon() {
        return XMaterial.WOODEN_PICKAXE;
    }

    @Override
    public ItemStack getItemStack() {
        return ItemBuilder.of(XMaterial.WOODEN_PICKAXE)
                .build();
    }
}
