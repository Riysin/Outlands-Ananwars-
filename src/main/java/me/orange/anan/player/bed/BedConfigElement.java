package me.orange.anan.player.bed;

import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.config.annotation.ElementType;

import java.util.ArrayList;
import java.util.List;

@ConfigurationElement
public class BedConfigElement {
    @ElementType(BedElement.class)
    private List<BedElement> bedList = new ArrayList<>();

    public List<BedElement> getBedList() {
        return bedList;
    }

    public void setBedList(List<BedElement> bedList) {
        this.bedList = bedList;
    }
}
