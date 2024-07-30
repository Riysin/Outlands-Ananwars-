package me.orange.anan.blocks.config;

import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.mc.util.Position;

@ConfigurationElement
public class BlockConfigElement {
    private int health = 0;
    private Position position = new Position();

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
