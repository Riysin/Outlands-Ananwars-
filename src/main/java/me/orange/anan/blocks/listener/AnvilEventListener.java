package me.orange.anan.blocks.listener;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.util.ItemLoreBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

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

        MCSchedulers.getGlobalScheduler().schedule(() -> {
            ItemStack itemStack = event.getInventory().getItem(2);

            if (event.getInventory().getType().equals(InventoryType.ANVIL)
                    && itemStack != null) {
                ItemStack fairyItem = craftManager.getItemStack(itemStack, player);
                fairyItem.getItemMeta().setDisplayName(itemStack.getItemMeta().getDisplayName());
                fairyItem.getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

                Map<Enchantment, Integer> enchantments = itemStack.getEnchantments();
                if (!enchantments.isEmpty())
                    enchantments.forEach(fairyItem::addUnsafeEnchantment);

                Craft craft = craftManager.getCraft(itemStack);
                fairyItem = ItemLoreBuilder.of(itemStack)
                        .setCraft(craftManager, craft)
                        .craftType()
                        .damage()
                        .description()
                        .enchantments()
                        .applyLore();

                event.getInventory().setItem(2, fairyItem);
            }
        },1);
    }
}
