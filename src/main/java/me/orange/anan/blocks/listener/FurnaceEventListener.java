package me.orange.anan.blocks.listener;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
@RegisterAsListener
public class FurnaceEventListener implements Listener {
    private final CraftManager craftManager;

    public FurnaceEventListener(CraftManager craftManager) {
        this.craftManager = craftManager;
    }

    @EventHandler
    public void onSmelt(FurnaceSmeltEvent event) {
        ItemStack item = event.getResult();
        Craft craft = craftManager.getCraft(item);

        event.setResult(craft.getItemStack());
    }

    @EventHandler
    public void onExtractFromFurnace(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getType() == InventoryType.FURNACE && event.getSlotType() == InventoryType.SlotType.RESULT) {
            ItemStack itemStack = craftManager.getItemStack(craftManager.getCraft(event.getCurrentItem()), player).clone();
            itemStack.setAmount(event.getCurrentItem().getAmount());
            event.setCurrentItem(itemStack);
        }
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        event.setExpToDrop(0);
    }
}
