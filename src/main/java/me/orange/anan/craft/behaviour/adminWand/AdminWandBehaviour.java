package me.orange.anan.craft.behaviour.adminWand;

import io.fairyproject.bukkit.listener.ListenerRegistry;
import io.fairyproject.bukkit.util.items.behaviour.ItemBehaviour;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.behaviour.CraftBehaviour;
import me.orange.anan.events.AdminRightClickWandEvent;
import me.orange.anan.events.AdminShiftRightClickWandEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.block.Action;

import java.util.Arrays;
import java.util.List;

@InjectableComponent
public class AdminWandBehaviour implements CraftBehaviour {
    private final ListenerRegistry listenerRegistry;

    public AdminWandBehaviour(ListenerRegistry listenerRegistry) {
        this.listenerRegistry = listenerRegistry;
    }

    @Override
    public String getID() {
        return "adminWand";
    }

    @Override
    public List<ItemBehaviour> getBehaviours() {
        return Arrays.asList(
                ItemBehaviour.interact(listenerRegistry, (player1, item, action, event) -> {
                    Bukkit.getPluginManager().callEvent(new AdminRightClickWandEvent(player1, event.getClickedBlock()));
                    event.setCancelled(true);
                }, Action.RIGHT_CLICK_BLOCK),
                ItemBehaviour.interact(listenerRegistry, (player1, item, action, event) -> {
                    if (player1.isSneaking()) {
                        Bukkit.getPluginManager().callEvent(new AdminShiftRightClickWandEvent(player1, event.getClickedBlock()));
                        event.setCancelled(true);
                    }
                }, Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)
        );
    }
}
