package me.orange.anan.craft.behaviour.teamCore;

import io.fairyproject.container.InjectableComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;

import java.util.*;

@InjectableComponent
public class TeamCoreManager {
    private TeamCoreConfig teamCoreConfig;
    private TeamCoreEventListener teamCoreEventListener;
    private List<TeamCore> teamCores = new ArrayList<>();

    public TeamCoreManager(TeamCoreConfig teamCoreConfig) {
        this.teamCoreConfig = teamCoreConfig;

        loadConfig();
    }

    public void loadConfig() {
        for (TeamCoreConfigElement element : teamCoreConfig.getTeamCores()) {
            teamCores.add(new TeamCore(element.getPlacePlayerUUID(), teamCoreEventListener.spawnTeamCore(new Location()) , element.getCoreBlockLocation().getBlock()));
            getTeamCore(element.getPlacePlayerUUID()).setConnectedBlocks(element.getConnectedBlocks());
        }
    }

    public void saveConfig() {
        teamCoreConfig.getTeamCores().clear();
        for (TeamCore teamCore : teamCores) {
            TeamCoreConfigElement element = new TeamCoreConfigElement();
            element.setPlacePlayerUUID(teamCore.getPlacePlayer());
            element.setCoreCreeperHealth(teamCore.getCoreCreeper().getHealth());
            element.setCoreBlockLocation(teamCore.getCoreBlock().getLocation());
            element.setConnectedBlocks(teamCore.getConnectedBlocks());
            teamCoreConfig.addTeamCore(element);
        }
    }

    public List<TeamCore> getTeamCores() {
        return this.teamCores;
    }

    public void addTeamCore(Player player, Block block, Creeper creeper) {
        teamCores.add(new TeamCore(player.getUniqueId(), creeper, block));
    }

    public void removeTeamCore(TeamCore teamCore) {
        teamCores.remove(teamCore.getPlacePlayer());
    }

    public TeamCore getTeamCore(Block block) {
        for (TeamCore teamCore : teamCores) {
            if (teamCore.getCoreBlock().equals(block)) {
                return teamCore;
            }
        }
        return null;
    }

    public TeamCore getTeamCore(UUID uuid) {
        for (TeamCore teamCore : teamCores) {
            if (teamCore.getPlacePlayer().equals(uuid)) {
                return teamCore;
            }
        }
        return null;
    }

    public TeamCore getTeamCore(Creeper creeper) {
        for (TeamCore teamCore : teamCores) {
            if (teamCore.getCoreCreeper().equals(creeper)) {
                return teamCore;
            }
        }
        return null;
    }

    public TeamCore getTeamCoreByLocation(Location location) {
        Block block = location.getBlock();
        for (TeamCore teamCore : teamCores) {
            if (teamCore.getConnectedBlocks().contains(block) || teamCore.getTerritoryBlocks().contains(block)) {
                return teamCore;
            }
        }
        return null;
    }

    public TeamCoreConfigElement getTeamCoreConfigElement(TeamCore teamCore) {
        return teamCoreConfig.getTeamCores().stream()
                .filter(element -> element.getPlacePlayerUUID().equals(teamCore.getPlacePlayer()))
                .findFirst()
                .orElse(null);
    }

}
