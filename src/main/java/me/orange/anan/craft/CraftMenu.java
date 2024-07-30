package me.orange.anan.craft;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.PaginatedPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@InjectableComponent
public class CraftMenu {
    private final GuiFactory guiFactory;
    private final CraftManager craftManager;
    private final ConfirmMenu confirmMenu;

    public CraftMenu(GuiFactory guiFactory, CraftManager craftManager, ConfirmMenu confirmMenu) {
        this.guiFactory = guiFactory;
        this.craftManager = craftManager;
        this.confirmMenu = confirmMenu;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("§f§lCraft Menu"));

        NormalPane pane = Pane.normal(9, 6);
        pane.setSlot(1, 0, GuiSlot.of(ItemBuilder.of(XMaterial.GRASS_BLOCK)
                .name("§7全部")
                .build()));
        pane.setSlot(2, 0, GuiSlot.of(ItemBuilder.of(XMaterial.WOODEN_SWORD)
                .name("§7武器")
                .build()));
        pane.setSlot(3, 0, GuiSlot.of(ItemBuilder.of(XMaterial.WOODEN_PICKAXE)
                .name("§7工具")
                .build()));
        pane.setSlot(4, 0, GuiSlot.of(ItemBuilder.of(XMaterial.LEATHER_HELMET)
                .name("§7裝備")
                .build()));
        pane.setSlot(5, 0, GuiSlot.of(ItemBuilder.of(XMaterial.OAK_PLANKS)
                .name("§7建材")
                .build()));
        pane.setSlot(6, 0, GuiSlot.of(ItemBuilder.of(XMaterial.CRAFTING_TABLE)
                .name("§7爆破物")
                .build()));
        pane.setSlot(7, 0, GuiSlot.of(ItemBuilder.of(XMaterial.TNT)
                .name("§7其他")
                .build()));
        // Add craftable item
        gui.onDrawCallback(updatePlayer -> {
            AtomicInteger i = new AtomicInteger(18);
            craftManager.getCrafts().forEach((k, craft) -> {
                if (craftManager.canCraft(player, craft)) {
                    List<String> loreLines = new ArrayList<>();
                    //setup recipe lore
                    for (ItemStack itemStack : craft.getRecipe()) {
                        String itemName = itemStack.getType().name();
                        loreLines.add("§7" + itemName);
                    }
                    loreLines.add("§f點擊合成此物品");

                    pane.setSlot(i.get(), GuiSlot.of(ItemBuilder.of(craft.getMenuIcon())
                            .clearLore()
                            .name(craft.getName())
                            .lore(loreLines)
                            .amount(1)
                            .build(), clicker -> {
                        confirmMenu.open(player, craft);
                        gui.update(clicker);
                    }));
                    i.incrementAndGet();
                }
            });

            craftManager.getCrafts().forEach((k, craft) -> {
                if (!craftManager.canCraft(player, craft)) {
                    List<String> loreLines = new ArrayList<>();
                    //setup recipe lore
                    for (ItemStack itemStack : craft.getRecipe()) {
                        String itemName = itemStack.getType().name();
                        boolean hasEnough = craftManager.hasEnough(player, itemStack);
                        loreLines.add((hasEnough ? "§7" : "§c") + itemName);
                    }
                    loreLines.add("§c材料不足");

                    pane.setSlot(i.get(), GuiSlot.of(ItemBuilder.of(craft.getMenuIcon())
                            .clearLore()
                            .name("§c" + craft.getName())
                            .lore(loreLines)
                            .amount(0)
                            .build(), clicker -> {
                        clicker.sendMessage("§cYou do not have enough material to craft this item");
                        gui.update(clicker);
                    }));
                    i.incrementAndGet();
                }
            });
        });

        pane.fillEmptySlots(GuiSlot.of(XMaterial.GRAY_STAINED_GLASS_PANE));

        gui.addPane(pane);

        gui.open(player);
    }
}
