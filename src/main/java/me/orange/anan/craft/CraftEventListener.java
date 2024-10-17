package me.orange.anan.craft;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.events.PlayerOpenInventoryEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;

@InjectableComponent
@RegisterAsListener
public class CraftEventListener implements Listener {
    @EventHandler
    public void craftItem(PrepareItemCraftEvent event) {
        Material itemType = event.getRecipe().getResult().getType();
        event.getViewers().forEach(player -> {
            player.sendMessage("§cYou cannot use this crafting system!");
            event.getInventory().setResult(ItemBuilder.of(XMaterial.AIR).build());
        });
    }

    @EventHandler
    public void onOpenCraftMenu(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getInventory().getType() == InventoryType.CRAFTING && event.getSlotType().equals(InventoryType.SlotType.CRAFTING)) {
            Player player = (Player) event.getWhoClicked();
            if (event.getSlot() == 1) {
                player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
                player.performCommand("craft menu");
                event.setCancelled(true);
            } else if (event.getSlot() == 2) {
                player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
                player.performCommand("craft ongoing");
                event.setCancelled(true);
            } else if (event.getSlot() == 3) {
                player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
                player.performCommand("player menu "+ player.getName());
                event.setCancelled(true);
            } else if (event.getSlot() == 4) {
                player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
                player.performCommand("settings");
                event.setCancelled(true);
            }
            if (event.getInventory().getItem(1) != null) {
                event.getView().getTopInventory().clear();
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(PlayerOpenInventoryEvent event) {
        if(event.getTopInventory().getType() == InventoryType.CRAFTING && event.getTopInventory().getItem(1) == null) {
            event.getTopInventory().setItem(1, ItemBuilder.of(XMaterial.CRAFTING_TABLE).name("§eCraft Menu").lore("§7Open the crafting menu").build());
            event.getTopInventory().setItem(2, ItemBuilder.of(XMaterial.ITEM_FRAME).name("§eCraft Ongoing").lore("§7Check your ongoing crafts").build());
            event.getTopInventory().setItem(3, ItemBuilder.of(XMaterial.PLAYER_HEAD).name("§ePlayerMenu").lore("§7Check your stats").skull(event.getPlayer().getName()).build());
            event.getTopInventory().setItem(4, ItemBuilder.of(XMaterial.BOOK).name("§eSettings").lore("§7Open the settings menu").build());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getType() == InventoryType.CRAFTING && event.getInventory().getItem(1) != null) {
            event.getView().getTopInventory().clear();
        }
    }
}

