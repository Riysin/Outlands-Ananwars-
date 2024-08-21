package me.orange.anan.craft.config;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.config.annotation.ConfigurationElement;
import me.orange.anan.craft.CraftTier;

import java.util.*;

@ConfigurationElement
public class CraftElement {
    private String id = "id";
    private XMaterial material = XMaterial.STONE;
    private XMaterial icon = XMaterial.STONE;
    private String displayName = "顯示名稱";
    private List<String> lore = Arrays.asList("lore1", "lore2");
    private int time = 0;
    private CraftTier tier = CraftTier.COMMON;
    private Map<String, Integer> recipes = new HashMap<>();

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public XMaterial getMaterial() {
        return material;
    }

    public void setMaterial(XMaterial material) {
        this.material = material;
    }

    public XMaterial getIcon() {
        return icon;
    }

    public void setIcon(XMaterial icon) {
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
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

    public Map<String, Integer> getRecipes() {
        return recipes;
    }

    public void setRecipes(Map<String, Integer> recipes) {
        this.recipes = recipes;
    }
}
