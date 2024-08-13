package me.orange.anan.player.bed;

import io.fairyproject.mc.util.Position;

import java.util.UUID;

public class Bed {
    private UUID owner;
    private String bedName;
    private Position position;

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public String getBedName() {
        return bedName;
    }

    public void setBedName(String bedName) {
        this.bedName = bedName;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
