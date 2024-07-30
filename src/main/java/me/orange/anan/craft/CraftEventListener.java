package me.orange.anan.craft;

import com.cryptomorin.xseries.XMaterial;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.Palette;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
@RegisterAsListener
public class CraftEventListener implements Listener {
    @EventHandler
    public void craftItem(PrepareItemCraftEvent event) {
        Material itemType = event.getRecipe().getResult().getType();
        event.getViewers().forEach(player -> {
            player.sendMessage("§cyou can't use this crafting system");
            event.getInventory().setResult(ItemBuilder.of(XMaterial.AIR).build());
        });
    }

    @EventHandler
    public void onOpenCraftMenu(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getSlotType() == InventoryType.SlotType.RESULT) {
                player.performCommand("craft menu");
                event.setCancelled(true);
            } else if (event.getSlot() == 1 && event.getSlotType().equals(InventoryType.SlotType.CRAFTING)) {
                player.performCommand("craft menu");
                event.setCancelled(true);
            } else if (event.getSlot() == 2 && event.getSlotType().equals(InventoryType.SlotType.CRAFTING)) {
                player.performCommand("craft ongoing");
                event.setCancelled(true);
            }
        }
    }

}

