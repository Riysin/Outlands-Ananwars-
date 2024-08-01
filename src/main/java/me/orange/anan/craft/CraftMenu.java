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
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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
                .build(),clicker->{
            removeItemOnCraftMenu(gui,pane);
            addItemToCraftMenu(gui,pane,clicker,CraftType.ALL);
            gui.update(clicker);
        }));
        pane.setSlot(2, 0, GuiSlot.of(ItemBuilder.of(XMaterial.WOODEN_SWORD)
                .name("§7戰鬥")
                .build(),clicker -> {
            removeItemOnCraftMenu(gui,pane);
            addItemToCraftMenu(gui,pane,clicker,CraftType.COMBAT);
            gui.update(clicker);
        }));
        pane.setSlot(3, 0, GuiSlot.of(ItemBuilder.of(XMaterial.WOODEN_PICKAXE)
                .name("§7工具")
                .build(),clicker->{
            removeItemOnCraftMenu(gui,pane);
            addItemToCraftMenu(gui,pane,clicker,CraftType.TOOL);
            gui.update(clicker);
        }));
        pane.setSlot(4, 0, GuiSlot.of(ItemBuilder.of(XMaterial.GUNPOWDER)
                .name("§7資源")
                .build(),clicker->{
            removeItemOnCraftMenu(gui,pane);
            addItemToCraftMenu(gui,pane,clicker,CraftType.RESOURCE);
            gui.update(clicker);
        }));
        pane.setSlot(5, 0, GuiSlot.of(ItemBuilder.of(XMaterial.OAK_PLANKS)
                .name("§7建材")
                .build(),clicker->{
            removeItemOnCraftMenu(gui,pane);
            addItemToCraftMenu(gui,pane,clicker,CraftType.BUILD);
            gui.update(clicker);
        }));
        pane.setSlot(6, 0, GuiSlot.of(ItemBuilder.of(XMaterial.CRAFTING_TABLE)
                .name("§7用途")
                .build(),clicker->{
            removeItemOnCraftMenu(gui,pane);
            addItemToCraftMenu(gui,pane,clicker,CraftType.USAGE);
            gui.update(clicker);
        }));
        pane.setSlot(7, 0, GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_DYE)
                .name("§7其他")
                .build(),clicker->{
            removeItemOnCraftMenu(gui,pane);
            addItemToCraftMenu(gui,pane,clicker,CraftType.MISC);
            gui.update(clicker);
        }));

        addItemToCraftMenu(gui, pane, player, CraftType.ALL);

        pane.fillEmptySlots(GuiSlot.of(XMaterial.GRAY_STAINED_GLASS_PANE));

        gui.addPane(pane);

        gui.open(player);
    }

    private void addItemToCraftMenu(Gui gui, NormalPane pane, Player player, CraftType craftType) {
        // Add craftable item
        gui.onDrawCallback(updatePlayer -> {
            List<GuiSlot> canCrafts = new ArrayList<>(), cannotCrafts = new ArrayList<>();

            craftManager.getCrafts().forEach((key, craft) -> {
                if (craftType != CraftType.ALL && craft.getType() != craftType || craft.getRecipe().isEmpty()) {
                    return;
                }
                List<String> loreLines = new ArrayList<>();
                boolean canCraft = craftManager.canCraft(player, craft);

                loreLines.add("§8" + craft.getType().name());
                loreLines.add("");
                loreLines.addAll(craft.getLore());
                loreLines.add("");
                loreLines.add("§e所需材料:");

                //setup recipe lore
                for (ItemStack itemStack : craft.getRecipe()) {
                    String itemName = itemStack.getItemMeta().getDisplayName();
                    int playerAmount = craftManager.getPlayerItemAmount(player, itemStack);
                    boolean hasEnough = craftManager.hasEnough(player, itemStack);
                    loreLines.add((hasEnough ? "§a   " : "§c   ")  + itemName + " §7(" + playerAmount + "/" + itemStack.getAmount() + ")");
                }

                loreLines.add("");
                loreLines.add((canCraft ? "§e點擊合成此物品" : "§c材料不足"));

                if (canCraft) {
                    canCrafts.add(GuiSlot.of(ItemBuilder.of(craft.getMenuIcon())
                            .clearLore()
                            .name("§e" + craft.getName())
                            .lore(loreLines)
                            .amount(1)
                            .itemFlag(ItemFlag.HIDE_ATTRIBUTES)
                            .build(), clicker -> {
                                confirmMenu.open(player, craft);
                                gui.update(clicker);
                            }));
                } else {
                    cannotCrafts.add(GuiSlot.of(ItemBuilder.of(craft.getMenuIcon())
                            .clearLore()
                            .name("§c" + craft.getName())
                            .lore(loreLines)
                            .amount(0)
                            .itemFlag(ItemFlag.HIDE_ATTRIBUTES)
                            .build(), clicker -> {
                                clicker.sendMessage("§cYou do not have enough material to craft this item");
                                clicker.playSound(clicker.getLocation(), Sound.VILLAGER_NO, 1 ,1);
                                gui.update(clicker);
                            }));
                }
            });

            AtomicInteger i = new AtomicInteger(18);
            Stream.concat(canCrafts.stream(), cannotCrafts.stream())
                    .forEach(slot -> pane.setSlot(i.getAndIncrement(), slot));
        });
    }

    private void removeItemOnCraftMenu(Gui gui,NormalPane pane) {
        gui.onDrawCallback($->{
            for (int i = 0; i < pane.getUsedSlots().length; i++) {
                pane.setSlot(i,2,GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE), gui::update));
            }
        });
    }
}
