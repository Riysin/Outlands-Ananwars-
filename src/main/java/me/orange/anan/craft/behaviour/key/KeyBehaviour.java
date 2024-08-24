package me.orange.anan.craft.behaviour.key;

import io.fairyproject.bukkit.listener.ListenerRegistry;
import io.fairyproject.bukkit.util.items.behaviour.ItemBehaviour;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.behaviour.CraftBehaviour;
import me.orange.anan.events.PlayerRightClickKeyEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.block.Action;

import java.util.Arrays;
import java.util.List;

@InjectableComponent
public class KeyBehaviour implements CraftBehaviour {
    private final ListenerRegistry listenerRegistry;

    public KeyBehaviour(ListenerRegistry listenerRegistry) {
        this.listenerRegistry = listenerRegistry;
    }

    @Override
    public String getID() {
        return "key";
    }

    @Override
    public List<ItemBehaviour> getBehaviours() {
        return Arrays.asList(
                ItemBehaviour.interact(listenerRegistry, (player1, item, action, event) -> {
                    Bukkit.getPluginManager().callEvent(new PlayerRightClickKeyEvent(player1, event.getClickedBlock()));
                    event.setCancelled(true);
                }, Action.RIGHT_CLICK_BLOCK)
        );
    }
}

