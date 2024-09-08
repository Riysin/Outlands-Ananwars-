package me.orange.anan.fishing;

import io.fairyproject.config.annotation.ConfigurationElement;

@ConfigurationElement
public class FishConfigElement{
    private int weight = 1;
    private int price = 1;
    private FishRegion region = FishRegion.ALL;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public FishRegion getRegion() {
        return region;
    }

    public void setRegion(FishRegion region) {
        this.region = region;
    }
}
