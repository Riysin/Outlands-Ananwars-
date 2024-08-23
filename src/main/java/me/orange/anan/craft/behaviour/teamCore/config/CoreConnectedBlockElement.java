package me.orange.anan.craft.behaviour.teamCore.config;

import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.mc.util.Position;

@ConfigurationElement
public class CoreConnectedBlockElement {
    private Position position = new Position();

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
