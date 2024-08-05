package me.orange.anan.craft;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.ProfileInputType;
import com.cryptomorin.xseries.profiles.objects.Profileable;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@InjectableComponent
public class ConfirmMenu {
    private final GuiFactory guiFactory;
    private final CraftManager craftManager;
    private final CraftTimerManager craftTimerManager;

    public ConfirmMenu(GuiFactory guiFactory, CraftManager craftManager, CraftManager craftManager1, CraftTimerManager craftTimerManager) {
        this.guiFactory = guiFactory;
        this.craftManager = craftManager1;
        this.craftTimerManager = craftTimerManager;
    }

    public void open(Player player, Craft craft) {
        Gui gui = guiFactory.create(Component.text("§f§l確認介面"));
        NormalPane pane = Pane.normal(9, 5);
        List<String> loreLines = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(1);
        int maxAmount = craftManager.getCanCraftAmount(player, craft);

        //minus amount
        pane.setSlot(2, 1, GuiSlot.of(ItemBuilder.of(XMaterial.PLAYER_HEAD).transformItemStack(itemStack -> {
            return XSkull.of(itemStack).profile(Profileable.of(ProfileInputType.BASE64, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ2YjEyOTNkYjcyOWQwMTBmNTM0Y2UxMzYxYmJjNTVhZTVhOGM4ZjgzYTE5NDdhZmU3YTg2NzMyZWZjMiJ9fX0=")).apply();
        }).name("§7-1").amount(1).build(), clicker -> {
            addCount(count, -1, maxAmount);
            gui.update(clicker);
        }));

        pane.setSlot(1, 1, GuiSlot.of(ItemBuilder.of(XMaterial.PLAYER_HEAD).transformItemStack(itemStack -> {
            return XSkull.of(itemStack).profile(Profileable.of(ProfileInputType.BASE64, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ2YjEyOTNkYjcyOWQwMTBmNTM0Y2UxMzYxYmJjNTVhZTVhOGM4ZjgzYTE5NDdhZmU3YTg2NzMyZWZjMiJ9fX0=")).apply();
        }).name("§7-10").amount(10).build(), clicker -> {
            addCount(count, -10, maxAmount);
            gui.update(clicker);
        }));

        pane.setSlot(0, 1, GuiSlot.of(ItemBuilder.of(XMaterial.PLAYER_HEAD).transformItemStack(itemStack -> {
            return XSkull.of(itemStack).profile(Profileable.of(ProfileInputType.BASE64, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ2YjEyOTNkYjcyOWQwMTBmNTM0Y2UxMzYxYmJjNTVhZTVhOGM4ZjgzYTE5NDdhZmU3YTg2NzMyZWZjMiJ9fX0=")).apply();
        }).name("§7min").amount(64).build(), clicker -> {
            addCount(count, -maxAmount, maxAmount);
            gui.update(clicker);
        }));

        //plus amount
        pane.setSlot(6, 1, GuiSlot.of(ItemBuilder.of(XMaterial.PLAYER_HEAD).transformItemStack(itemStack -> {
            return XSkull.of(itemStack).profile(Profileable.of(ProfileInputType.BASE64, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdhMGZjNmRjZjczOWMxMWZlY2U0M2NkZDE4NGRlYTc5MWNmNzU3YmY3YmQ5MTUzNmZkYmM5NmZhNDdhY2ZiIn19fQ==")).apply();
        }).name("§7+1").amount(1).build(), clicker -> {
            addCount(count, 1, maxAmount);
            gui.update(clicker);
        }));
        pane.setSlot(7, 1, GuiSlot.of(ItemBuilder.of(XMaterial.PLAYER_HEAD).transformItemStack(itemStack -> {
            return XSkull.of(itemStack).profile(Profileable.of(ProfileInputType.BASE64, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdhMGZjNmRjZjczOWMxMWZlY2U0M2NkZDE4NGRlYTc5MWNmNzU3YmY3YmQ5MTUzNmZkYmM5NmZhNDdhY2ZiIn19fQ==")).apply();
        }).name("§7+10").amount(10).build(), clicker -> {
            addCount(count, 10, maxAmount);
            gui.update(clicker);
        }));
        pane.setSlot(8, 1, GuiSlot.of(ItemBuilder.of(XMaterial.PLAYER_HEAD).transformItemStack(itemStack -> {
            return XSkull.of(itemStack).profile(Profileable.of(ProfileInputType.BASE64, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdhMGZjNmRjZjczOWMxMWZlY2U0M2NkZDE4NGRlYTc5MWNmNzU3YmY3YmQ5MTUzNmZkYmM5NmZhNDdhY2ZiIn19fQ==")).apply();
        }).name("§7Max").amount(64).build(), clicker -> {
            addCount(count, maxAmount, maxAmount);
            gui.update(clicker);
        }));

        //crafted item
        gui.onDrawCallback(updatePlayer -> {
            loreLines.clear();
            loreLines.add("§8" + craft.getType().name());
            loreLines.add("");
            loreLines.addAll(craft.getLore());
            loreLines.add("");
            loreLines.add("§f製作: §l§a" + count + "§f個");
            loreLines.add("§e所需材料:");

            //setup recipe lore
            for (ItemStack itemStack : craft.getRecipe()) {
                String itemName = itemStack.getItemMeta().getDisplayName();
                int playerAmount = craftManager.getPlayerItemAmount(player, itemStack);
                loreLines.add("§e" + itemName + " §7(" + playerAmount + "/" + itemStack.getAmount() * count.get() + ")");
            }


            pane.setSlot(4, 1, GuiSlot.of(ItemBuilder.of(craft.getMenuIcon())
                    .clearLore()
                    .lore(loreLines)
                    .amount(count.get())
                    .build()
            ));
        });

        pane.setSlot(3, 3, GuiSlot.of(ItemBuilder.of(XMaterial.GREEN_STAINED_GLASS_PANE)
                .name("§7確認")
                .build(), player1 -> {
            player1.closeInventory();
            craftTimerManager.addCraftTimer(player1, craft);

            for (ItemStack item : craft.getRecipe()) {
                removeItemsFromInventory(player1, item);
            }
        }));
        pane.setSlot(5, 3, GuiSlot.of(ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE)
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

    private AtomicInteger addCount(AtomicInteger count, int i, int max) {
        count.getAndAdd(i);
        if (count.get() > max) {
            count.set(max);
            return count;
        } else if (count.get() < 1) {
            count.set(1);
            return count;
        } else {
            return count;
        }
    }
}
