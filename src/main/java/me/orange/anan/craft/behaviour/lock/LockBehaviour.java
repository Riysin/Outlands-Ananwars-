package me.orange.anan.craft.behaviour.lock;

import io.fairyproject.bukkit.listener.ListenerRegistry;
import io.fairyproject.bukkit.util.items.behaviour.ItemBehaviour;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.behaviour.CraftBehaviour;
import me.orange.anan.events.PlayerRightClickLockEvent;
import me.orange.anan.events.TeamCoreInventoryOpenEvent;
import me.orange.anan.events.TeamCoreLockEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.block.Action;

import java.util.Arrays;
import java.util.List;

@InjectableComponent
public class LockBehaviour implements CraftBehaviour {
    private final ListenerRegistry listenerRegistry;

    public LockBehaviour(ListenerRegistry listenerRegistry) {
        this.listenerRegistry = listenerRegistry;
    }

    @Override
    public String getID() {
        return "lock";
    }

    @Override
    public List<ItemBehaviour> getBehaviours() {
        return Arrays.asList(
                ItemBehaviour.interact(listenerRegistry, (player1, item, action, event) -> {
                    Bukkit.getPluginManager().callEvent(new PlayerRightClickLockEvent(player1, event.getClickedBlock()));
                    event.setCancelled(true);
                }, Action.RIGHT_CLICK_BLOCK)
        );
    }
}

