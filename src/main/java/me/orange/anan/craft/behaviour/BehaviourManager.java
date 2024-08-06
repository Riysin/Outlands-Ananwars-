package me.orange.anan.craft.behaviour;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.behaviour.upgradeHammer.UpgradeHammer;

import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class BehaviourManager {
    private Map<String, CraftBehaviour> craftBehaviours = new HashMap<>();

    public BehaviourManager(UpgradeHammer upgradeHammer){
        registerCraftBehaviour(upgradeHammer);
    }

    public void registerCraftBehaviour(CraftBehaviour craftBehaviour){
        craftBehaviours.put(craftBehaviour.getID(), craftBehaviour);
    }

    public Map<String, CraftBehaviour> getBehaviours(){
        return craftBehaviours;
    }
}
