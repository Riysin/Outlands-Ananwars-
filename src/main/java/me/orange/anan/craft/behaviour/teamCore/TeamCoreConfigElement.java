package me.orange.anan.craft.behaviour.teamCore;

import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.mc.util.Position;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ConfigurationElement
public class TeamCoreConfigElement {
    private String placePlayerUUID;
    private double coreCreeperHealth;
    private Position coreBlockPosition = new Position();
    private Set<Position> connectedBlocks;

    public UUID getPlacePlayerUUID() {
        return UUID.fromString(placePlayerUUID);
    }

    public void setPlacePlayerUUID(UUID placePlayerUUID) {
        this.placePlayerUUID = placePlayerUUID.toString();
    }

    public double getCoreCreeperHealth() {
        return coreCreeperHealth;
    }

    public void setCoreCreeperHealth(double coreCreeperHealth) {
        this.coreCreeperHealth = coreCreeperHealth;
    }

    public Location getCoreBlockLocation() {
        return BukkitPos.toBukkitLocation(coreBlockPosition);
    }

    public void setCoreBlockLocation(Location location) {
        this.coreBlockPosition = BukkitPos.toMCPos(location);
    }

    public Set<Block> getConnectedBlocks() {
        Set<Block> blocks = new HashSet<>();
        for (Position position : connectedBlocks) {
            blocks.add(BukkitPos.toBukkitLocation(position).getBlock());
        }
        return blocks;
    }

    public void setConnectedBlocks(Set<Block> connectedBlocks) {
        Set<Position> positions = new HashSet<>();
        for (Block block : connectedBlocks) {
            positions.add(BukkitPos.toMCPos(block.getLocation()));
        }
    }
}
