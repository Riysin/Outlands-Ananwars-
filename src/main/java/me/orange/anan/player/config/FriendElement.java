package me.orange.anan.player.config;

import io.fairyproject.config.annotation.ConfigurationElement;

import java.util.UUID;

@ConfigurationElement
public class FriendElement {
    private String uuid = "";

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid.toString();
    }
}
