package me.orange.anan.craft.behaviour.teamCore;

import io.fairyproject.container.InjectableComponent;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;

import java.util.*;

@InjectableComponent
public class TeamCoreManager {
    private List<TeamCore> teamCores = new ArrayList<>();

    public List<TeamCore> getTeamCores(){
        return this.teamCores;
    }

    public void addTeamCore(Player player, Block block, Creeper creeper){
        teamCores.add(new TeamCore(player.getUniqueId(), creeper, block));
    }

    public void removeTeamCore(TeamCore teamCore){
        teamCores.remove(teamCore.getPlacePlayer());
    }

    public TeamCore getTeamCore(Block block){
        for(TeamCore teamCore : teamCores){
            if(teamCore.getCoreBlock().equals(block)){
                return teamCore;
            }
        }
        return null;
    }

    public TeamCore getTeamCore(UUID uuid){
        for(TeamCore teamCore : teamCores){
            if(teamCore.getPlacePlayer().equals(uuid)){
                return teamCore;
            }
        }
        return null;
    }

    public TeamCore getTeamCore(Creeper creeper){
        for(TeamCore teamCore : teamCores){
            if(teamCore.getCoreCreeper().equals(creeper)){
                return teamCore;
            }
        }
        return null;
    }
}
