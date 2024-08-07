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
}
