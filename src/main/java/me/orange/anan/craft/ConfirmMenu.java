package me.orange.anan.craft;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
public class ConfirmMenu {
    private final GuiFactory guiFactory;
    private final CraftTimerManager craftTimerManager;

    public ConfirmMenu(GuiFactory guiFactory, CraftManager craftManager, CraftTimerManager craftTimerManager) {
        this.guiFactory = guiFactory;
        this.craftTimerManager = craftTimerManager;
    }

    public void open(Player player, Craft craft) {
        Gui gui = guiFactory.create(Component.text("§f§l確認介面"));
        NormalPane pane = Pane.normal(9, 3);

        pane.setSlot(1, 1, GuiSlot.of(ItemBuilder.of(XMaterial.GREEN_STAINED_GLASS_PANE)
                .name("§7確認")
                .build(), player1 -> {
            player1.closeInventory();
            craftTimerManager.addCraftTimer(player1, craft);

            for(ItemStack item : craft.getRecipe()) {
                removeItemsFromInventory(player1, item);
            }
        }));

        pane.setSlot(4, 1, GuiSlot.of(craft.getItemStack()));
        pane.setSlot(7, 1, GuiSlot.of(ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE)
                .name("§7返回")
                .build(), player1 -> {
            pane.fillEmptySlots(GuiSlot.of(XMaterial.GRAY_STAINED_GLASS_PANE));
            Bukkit.getServer().dispatchCommand(player1, "craft menu");
        }));

        pane.fillEmptySlots(GuiSlot.of(XMaterial.GRAY_STAINED_GLASS_PANE));

        gui.addPane(pane);
        gui.open(player);
    }

    private void removeItemsFromInventory(Player player, ItemStack itemStack) {
        int amountToRemove = itemStack.getAmount();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.isSimilar(itemStack)) {
                int itemAmount = item.getAmount();
                if (itemAmount > amountToRemove) {
                    item.setAmount(itemAmount - amountToRemove);
                    break;
                } else {
                    player.getInventory().remove(item);
                    amountToRemove -= itemAmount;
                    if (amountToRemove <= 0) {
                        break;
                    }
                }
            }
        }
    }
}
