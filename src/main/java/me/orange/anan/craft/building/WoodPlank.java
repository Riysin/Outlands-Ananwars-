package me.orange.anan.craft.building;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftTier;
import me.orange.anan.craft.CraftType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class WoodPlank implements Craft {
    @Override
    public String getID() {
        return "woodPlank";
    }

    @Override
    public String getName() {
        return "木材";
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
    public List<ItemStack> getRecipe() {
        return Arrays.asList(
                ItemBuilder.of(XMaterial.STICK)
                        .amount(2)
                        .tag("stick","resource")
                        .build()
        );
    }

    @Override
    public XMaterial getMenuIcon() {
        return XMaterial.OAK_PLANKS;
    }

    @Override
    public ItemStack getItemStack() {
        return ItemBuilder.of(XMaterial.OAK_PLANKS)
                .name("木材")
                .lore("§f這是一個木材", "§f可以送神")
                .tag("kurWood","resource")
                .build();
    }
}
