package me.orange.anan.craft.behaviour.lock;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class LockConfig extends YamlConfiguration {
    public LockConfig(Anan plugin) {
        super(plugin.getDataFolder().resolve("locks.yml"));
        this.loadAndSave();
    }

    @ElementType(LockConfigElement.class)
    private List<LockConfigElement> locks = new ArrayList<>();

    public List<LockConfigElement> getLocks() {
        return locks;
    }

    public void setLocks(List<LockConfigElement> locks) {
        this.locks = locks;
        this.save();
    }
}
