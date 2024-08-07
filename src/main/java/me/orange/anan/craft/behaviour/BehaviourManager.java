package me.orange.anan.craft.behaviour;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.behaviour.hammer.HammerBehaviour;
import me.orange.anan.craft.behaviour.teamCore.TeamCoreBehaviour;

import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class BehaviourManager {
    private Map<String, CraftBehaviour> craftBehaviours = new HashMap<>();

    public BehaviourManager(
            HammerBehaviour hammerBehaviour,
            TeamCoreBehaviour teamCoreBehaviour
    ){
        registerCraftBehaviour(hammerBehaviour);
        registerCraftBehaviour(teamCoreBehaviour);

    }

    public void registerCraftBehaviour(CraftBehaviour craftBehaviour){
        craftBehaviours.put(craftBehaviour.getID(), craftBehaviour);
    }

    public Map<String, CraftBehaviour> getBehaviours(){
        return craftBehaviours;
    }
}
