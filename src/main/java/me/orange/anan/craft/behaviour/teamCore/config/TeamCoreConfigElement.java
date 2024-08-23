package me.orange.anan.craft.behaviour.teamCore.config;

import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.mc.util.Position;
import me.orange.anan.craft.behaviour.teamCore.TeamCoreManager;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.*;

@ConfigurationElement
public class TeamCoreConfigElement {
    private String placePlayerUUID = "";
    private double coreCreeperHealth = 0.0;
    private Position coreCreeperPosition = new Position();
    private Position coreBlockPosition = new Position();

    @ElementType(CoreConnectedBlockElement.class)
    private List<CoreConnectedBlockElement> connectedBlocks = new ArrayList<>();

    @ElementType(TeamCoreInventoryElement.class)
    private List<TeamCoreInventoryElement> inventories = new ArrayList<>();

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
        for (CoreConnectedBlockElement element : connectedBlocks) {
            Location location = BukkitPos.toBukkitLocation(element.getPosition());
            blocks.add(location.getBlock());
        }
        return Collections.unmodifiableSet(blocks);  // Return immutable set
    }

    public void setConnectedBlocks(Set<Block> blocks) {
        if (blocks == null) {
            connectedBlocks.clear();
            return;
        }
        connectedBlocks.clear();
        for (Block block : blocks) {
            CoreConnectedBlockElement element = new CoreConnectedBlockElement();
            element.setPosition(BukkitPos.toMCPos(block.getLocation()));
            connectedBlocks.add(element);
        }
    }

    public List<TeamCoreInventoryElement> getInventories() {
        return inventories;
    }

    public void addInventory(TeamCoreInventoryElement inventory) {
        inventories.add(inventory);
    }

    public void setInventories(List<TeamCoreInventoryElement> inventories) {
        this.inventories = inventories;
    }
}
