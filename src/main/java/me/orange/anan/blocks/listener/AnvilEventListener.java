package me.orange.anan.blocks.listener;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.CraftManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
@RegisterAsListener
public class AnvilEventListener implements Listener {
    private final CraftManager craftManager;

    public AnvilEventListener(CraftManager craftManager) {
        this.craftManager = craftManager;
    }

    @EventHandler
    public void onAnvil(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getType().equals(InventoryType.ANVIL) && event.getSlotType().equals(InventoryType.SlotType.RESULT)) {
            ItemStack itemStack = craftManager.getItemStack(event.getCurrentItem(), player);
            event.setCurrentItem(itemStack);
        }
    }
}
