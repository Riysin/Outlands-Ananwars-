package me.orange.anan.craft;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.nbt.NBTKey;
import io.fairyproject.bukkit.nbt.NBTModifier;
import io.fairyproject.bukkit.util.items.behaviour.ItemBehaviour;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Craft {
    private ItemStack itemStack;
    private Map<String, Integer> recipe = new HashMap<>();
    private int time = 0;
    private CraftTier tier = CraftTier.COMMON;
    private CraftType type = CraftType.ALL;
    private XMaterial menuIcon = XMaterial.STONE;
    private List<ItemBehaviour> itemBehaviours = new ArrayList<>();

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String getName() {
        return getItemStack().getItemMeta().getDisplayName();
    }

    public String getID() {
        return NBTModifier.get().getString(this.itemStack, NBTKey.create("craft"));
    }

    public List<String> getLore() {
        return getItemStack().getItemMeta().getLore();
    }

    public Map<String, Integer> getRecipe() {
        return recipe;
    }

    public void setRecipe(Map<String, Integer> recipe) {
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

    public void addBehavior(ItemBehaviour behaviour){
        this.itemBehaviours.add(behaviour);
    }
}
