package me.orange.anan.craft.behaviour.teamCore;

import io.fairyproject.container.InjectableComponent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;

import java.util.*;

@InjectableComponent
public class TeamCoreManager {
    private final TeamCoreConfig teamCoreConfig;
    private final TeamCoreEventListener teamCoreEventListener;
    private List<TeamCore> teamCores = new ArrayList<>();

    public TeamCoreManager(TeamCoreConfig teamCoreConfig, TeamCoreEventListener teamCoreEventListener) {
        this.teamCoreConfig = teamCoreConfig;
        this.teamCoreEventListener = teamCoreEventListener;
        loadConfig();
    }

    // Load configuration into team cores list
    public void loadConfig() {
        for (TeamCoreConfigElement element : teamCoreConfig.getTeamCores()) {
            Creeper creeper = teamCoreEventListener.spawnTeamCore(element.getCoreCreeperLocation(), element.getCoreCreeperLocation().getWorld());
            TeamCore teamCore = new TeamCore(element.getPlacePlayerUUID(), creeper, element.getCoreBlockLocation().getBlock());
            teamCore.getCoreCreeper().setHealth(element.getCoreCreeperHealth());
            teamCores.add(teamCore);
            // Uncomment if connectedBlocks are needed
            // teamCore.setConnectedBlocks(element.getConnectedBlocks());
        }
    }

    // Save the current state of team cores to the configuration
    public void saveConfig() {
        teamCoreConfig.getTeamCores().clear();
        for (TeamCore teamCore : teamCores) {
            TeamCoreConfigElement element = new TeamCoreConfigElement();
            element.setPlacePlayerUUID(teamCore.getPlacePlayer());
            element.setCoreCreeperHealth(teamCore.getCoreCreeper().getHealth());
            element.setCoreCreeperPosition(teamCore.getCoreCreeper().getLocation());
            element.setCoreBlockLocation(teamCore.getCoreBlock().getLocation());
            // Uncomment if connectedBlocks are needed
            // element.setConnectedBlocks(teamCore.getConnectedBlocks());
            teamCoreConfig.addTeamCore(element);
        }
    }

    // Getter for team cores list
    public List<TeamCore> getTeamCores() {
        return this.teamCores;
    }

    // Add a new team core
    public void addTeamCore(Player player, Creeper creeper, Block block) {
        teamCores.add(new TeamCore(player.getUniqueId(), creeper, block));
    }

    // Remove a team core
    public void removeTeamCore(TeamCore teamCore) {
        teamCores.remove(teamCore); // Changed from teamCore.getPlacePlayer()
    }

    // Get team core by block location
    public TeamCore getTeamCore(Block block) {
        for (TeamCore teamCore : teamCores) {
            if (teamCore.getCoreBlock().equals(block)) {
                return teamCore;
            }
        }
        return null;
    }

    // Get team core by player UUID
    public TeamCore getTeamCore(UUID uuid) {
        for (TeamCore teamCore : teamCores) {
            if (teamCore.getPlacePlayer().equals(uuid)) {
                return teamCore;
            }
        }
        return null;
    }

    // Get team core by creeper entity
    public TeamCore getTeamCore(Creeper creeper) {
        for (TeamCore teamCore : teamCores) {
            if (teamCore.getCoreCreeper().equals(creeper)) {
                return teamCore;
            }
        }
        return null;
    }

    // Get team core by a specific location
    public TeamCore getTeamCoreByLocation(Location location) {
        Block block = location.getBlock();
        for (TeamCore teamCore : teamCores) {
            // Ensure the connectedBlocks and territoryBlocks are initialized
            if (teamCore.getConnectedBlocks().contains(block) || teamCore.getTerritoryBlocks().contains(block)) {
                return teamCore;
            }
        }
        return null;
    }

    // Get the configuration element for a given team core
    public TeamCoreConfigElement getTeamCoreConfigElement(TeamCore teamCore) {
        return teamCoreConfig.getTeamCores().stream()
                .filter(element -> element.getPlacePlayerUUID().equals(teamCore.getPlacePlayer()))
                .findFirst()
                .orElse(null);
    }
}
