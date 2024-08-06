package me.orange.anan.craft.config;

import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.config.annotation.ElementType;
import me.orange.anan.craft.CraftType;

import java.util.*;

@ConfigurationElement
public class CraftConfigElement {

    private CraftType craftType = CraftType.ALL;

    @ElementType(CraftElement.class)
    private List<CraftElement> crafts = new ArrayList<>();

    public CraftType getCraftType(){
        return this.craftType;
    }
    public void setCraftType(CraftType craftType){
        this.craftType = craftType;
    }
    public List<CraftElement> getCrafts() {
        return crafts;
    }
}
