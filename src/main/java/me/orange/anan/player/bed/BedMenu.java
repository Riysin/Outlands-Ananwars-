package me.orange.anan.player.bed;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@InjectableComponent
public class BedMenu implements Listener {
    private final GuiFactory guiFactory;
    private final Inventory anvil;

    public BedMenu(GuiFactory guiFactory) {
        this.guiFactory = guiFactory;
        this.anvil = Bukkit.createInventory(null, InventoryType.ANVIL, "Rename Bed");;
        anvil.setItem(0, ItemBuilder.of(XMaterial.PAPER).name("§7Rename Bed").build());
    }

    public void open(Player player){
        Gui gui = guiFactory.create(Component.text("§f§lBed Menu"));
        NormalPane pane = Pane.normal(9, 3);

        pane.setSlot(4, 1, GuiSlot.of(ItemBuilder.of(XMaterial.RED_BED)
                .name("§7Bed")
                .lore("§7Click to rename bed")
                .build(), clicker -> {
            player.openInventory(anvil);
        }));

        gui.addPane(pane);
        gui.open(player);
    }

    @RegisterAsListener
    class BedMenuListener implements Listener {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if (event.getInventory().equals(anvil)) {
                String name = event.getCurrentItem().getItemMeta().getDisplayName();
                event.getWhoClicked().sendMessage("§7You renamed the bed to " + name);
            }
        }
    }
}
