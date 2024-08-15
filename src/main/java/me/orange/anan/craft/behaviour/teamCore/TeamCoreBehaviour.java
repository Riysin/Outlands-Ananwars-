package me.orange.anan.craft.behaviour.teamCore;

import io.fairyproject.bukkit.listener.ListenerRegistry;
import io.fairyproject.bukkit.util.items.behaviour.ItemBehaviour;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.behaviour.CraftBehaviour;
import me.orange.anan.events.PlayerLeftClickHammerEvent;
import me.orange.anan.events.PlayerPlaceTeamCoreEvent;
import me.orange.anan.events.PlayerRightClickHammerEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.block.Action;

import java.util.Arrays;
import java.util.List;

@InjectableComponent
public class TeamCoreBehaviour implements CraftBehaviour {
    private final ListenerRegistry listenerRegistry;

    public TeamCoreBehaviour(ListenerRegistry listenerRegistry) {
        this.listenerRegistry = listenerRegistry;
    }

    @Override
    public String getID() {
        return "teamCore";
    }

    @Override
    public List<ItemBehaviour> getBehaviours() {
        return Arrays.asList(
                ItemBehaviour.blockPlace(listenerRegistry, (player, itemStack, block, event) -> {
                    PlayerPlaceTeamCoreEvent placeEvent = new PlayerPlaceTeamCoreEvent(player, block);
                    Bukkit.getPluginManager().callEvent(placeEvent);

                    if (placeEvent.isCancelled())
                        event.setCancelled(true);
                })
        );
    }
}
