package me.orange.anan.craft.behaviour.adminWand;

import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class AdminWandManager {
    private Map<Player, AdminWandAction> actionMap = new HashMap<>();

    public Map<Player, AdminWandAction> getActionMap() {
        return actionMap;
    }

    public void setActionMap(Map<Player, AdminWandAction> actionMap) {
        this.actionMap = actionMap;
    }

    public AdminWandAction getAction(Player player){
        if(!actionMap.containsKey(player)){
            actionMap.put(player, AdminWandAction.NONE);
        }
        return actionMap.get(player);
    }

    public void setAction(Player player, AdminWandAction action){
        actionMap.put(player, action);
    }
}
