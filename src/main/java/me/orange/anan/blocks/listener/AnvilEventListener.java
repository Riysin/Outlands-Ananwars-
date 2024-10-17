package me.orange.anan.blocks.listener;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
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
        if (event.getInventory().getType().equals(InventoryType.ANVIL) && event.getSlotType().equals(InventoryType.SlotType.RESULT)) {
            ItemStack itemStack = event.getCurrentItem();
            ItemStack fairyItem = craftManager.getItemStack(itemStack,player);
            fairyItem.getItemMeta().setDisplayName(itemStack.getItemMeta().getDisplayName());

            Map<Enchantment, Integer> enchantments = itemStack.getEnchantments();
            if (!enchantments.isEmpty())
                enchantments.forEach(fairyItem::addUnsafeEnchantment);

            Craft craft = craftManager.getCraft(itemStack);
            fairyItem = ItemLoreBuilder.of(itemStack)
                    .setCraft(craftManager,craft)
                    .craftType()
                    .damage()
                    .description()
                    .enchantments()
                    .applyLore();

            event.setCurrentItem(fairyItem);
        }
    }
}
