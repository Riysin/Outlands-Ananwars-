package me.orange.anan.craft.behaviour;

import io.fairyproject.bukkit.util.items.behaviour.ItemBehaviour;

import java.util.List;

public interface CraftBehaviour {
    String getID();
    List<ItemBehaviour> getBehaviours();
}
