package me.orange.anan.craft.behaviour.teamCore;

import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.mc.util.Position;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.*;

@ConfigurationElement
public class TeamCoreConfigElement {
    private String placePlayerUUID = "";
    private double coreCreeperHealth = 0.0;
    private Position coreCreeperPosition = new Position();
    private Position coreBlockPosition = new Position();
    private List<Position> connectedBlocks = new ArrayList<>();

    public UUID getPlacePlayerUUID() {
        return placePlayerUUID != null ? UUID.fromString(placePlayerUUID) : null;
    }

    public void setPlacePlayerUUID(UUID placePlayerUUID) {
        this.placePlayerUUID = placePlayerUUID != null ? placePlayerUUID.toString() : null;
    }

    public double getCoreCreeperHealth() {
        return coreCreeperHealth;
    }

    public void setCoreCreeperHealth(double coreCreeperHealth) {
        this.coreCreeperHealth = coreCreeperHealth;
    }

    public Location getCoreCreeperLocation() {
        return BukkitPos.toBukkitLocation(coreCreeperPosition);
    }

    public void setCoreCreeperPosition(Location location) {
        this.coreCreeperPosition = BukkitPos.toMCPos(location);
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
        this.connectedBlocks.clear();
        for (Block block : connectedBlocks) {
            this.connectedBlocks.add(BukkitPos.toMCPos(block.getLocation()));
        }
    }
}
