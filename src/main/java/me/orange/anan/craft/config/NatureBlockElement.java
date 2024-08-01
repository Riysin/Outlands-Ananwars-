package me.orange.anan.craft.config;

import io.fairyproject.config.annotation.ConfigurationElement;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationElement
public class NatureBlockElement {
    private Integer blockId = 1;
    private Integer data = -1;
    private Map<String, Integer> drops = new HashMap<>();

    public Map<String, Integer> getDrops() {
        return drops;
    }

    public void setDrops(Map<String, Integer> drops) {
        this.drops = drops;
    }

    public Integer getBlockId() {
        return blockId;
    }

    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }
}
