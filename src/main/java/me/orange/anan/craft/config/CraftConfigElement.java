package me.orange.anan.craft.config;

import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.config.annotation.ElementType;

import java.util.HashMap;
import java.util.Map;

@ConfigurationElement
public class CraftConfigElement {
    @ElementType(CraftElement.class)
    private Map<String, CraftElement> crafts = new HashMap<>();

    public Map<String, CraftElement> getCrafts() {
        return crafts;
    }
}
