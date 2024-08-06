package me.orange.anan.craft.behaviour.upgradeHammer;

import io.fairyproject.bukkit.listener.ListenerRegistry;
import io.fairyproject.bukkit.util.items.behaviour.ItemBehaviour;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.behaviour.CraftBehaviour;
import org.bukkit.event.block.Action;

import java.util.Arrays;
import java.util.List;

@InjectableComponent
public class UpgradeHammer implements CraftBehaviour {
    private final ListenerRegistry listenerRegistry;

    public UpgradeHammer(ListenerRegistry listenerRegistry) {
        this.listenerRegistry = listenerRegistry;
    }

    @Override
    public String getID() {
        return "upgradeHammer";
    }

    @Override
    public List<ItemBehaviour> getBehaviours() {
        return Arrays.asList(
                ItemBehaviour.interact(listenerRegistry, (player1, item, action, event) -> {
                    player1.sendMessage("You right-clicked a hammer!");
                }, Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK),
                ItemBehaviour.interact(listenerRegistry, (player1, item, action, event) -> {
                    player1.sendMessage("You left-clicked a hammer!");
                }, Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK),
                ItemBehaviour.interact(listenerRegistry, (player1, item, action, event) -> {
                    if(player1.isSneaking())
                        player1.sendMessage("You sneaking right-clicked a hammer!");
                }, Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)
        );
    }
}
