package me.orange.anan.craft.behaviour.hammer;

import io.fairyproject.bukkit.listener.ListenerRegistry;
import io.fairyproject.bukkit.util.items.behaviour.ItemBehaviour;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.behaviour.CraftBehaviour;
import me.orange.anan.events.PlayerLeftClickHammerEvent;
import me.orange.anan.events.PlayerRightClickHammerEvent;
import me.orange.anan.events.PlayerShiftRightClickHammerEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;

import java.util.Arrays;
import java.util.List;

@InjectableComponent
public class HammerBehaviour implements CraftBehaviour {
    private final ListenerRegistry listenerRegistry;

    public HammerBehaviour(ListenerRegistry listenerRegistry) {
        this.listenerRegistry = listenerRegistry;
    }

    @Override
    public String getID() {
        return "hammer";
    }

    @Override
    public List<ItemBehaviour> getBehaviours() {
        return Arrays.asList(
                ItemBehaviour.interact(listenerRegistry, (player1, item, action, event) -> {
                    if (player1.isSneaking()) {
                        Bukkit.getPluginManager().callEvent(new PlayerShiftRightClickHammerEvent(player1));
                        return;
                    }
                    if (event.getClickedBlock() != null) {
                        Bukkit.getPluginManager().callEvent(new PlayerRightClickHammerEvent(player1, event.getClickedBlock()));
                    } else {
                        player1.sendMessage("§e請選擇要升級的方塊");
                    }

                }, Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK),
                ItemBehaviour.interact(listenerRegistry, (player1, item, action, event) -> {
                    if (event.getClickedBlock() != null) {
                        Bukkit.getPluginManager().callEvent(new PlayerLeftClickHammerEvent(player1, event.getClickedBlock()));
                    } else {
                        player1.sendMessage("§e請選擇要修復的方塊");
                    }
                }, Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK)
        );
    }
}
