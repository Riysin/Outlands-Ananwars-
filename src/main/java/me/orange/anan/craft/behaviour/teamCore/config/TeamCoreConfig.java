package me.orange.anan.craft.behaviour.teamCore.config;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class TeamCoreConfig extends YamlConfiguration {
    public TeamCoreConfig(Anan plugin) {
        super(plugin.getDataFolder().resolve("teamCore.yml"));
        this.loadAndSave();
    }

    @ElementType(TeamCoreConfigElement.class)
    List<TeamCoreConfigElement> teamCores = new ArrayList<>();

    public List<TeamCoreConfigElement> getTeamCores() {
        return teamCores;
    }

    public void setTeamCores(List<TeamCoreConfigElement> teamCores) {
        this.teamCores = teamCores;
    }

    public void addTeamCore(TeamCoreConfigElement teamCore) {
        teamCores.add(teamCore);
        this.save();
    }

}
