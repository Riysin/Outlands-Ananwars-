package me.orange.anan.clan;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;

import java.util.HashMap;
import java.util.Map;

public enum NametagVisibility {
    never, always, hideForOtherTeams, hideForOwnTeams;
}
