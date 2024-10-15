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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
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
            itemStack.getItemMeta().setDisplayName(itemStack.getItemMeta().getDisplayName());
        }
    }

    @EventHandler
    public void onAnvilInventoryOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();
        if (inventory.getType().equals(InventoryType.ANVIL)) {
            ItemStack itemStack = inventory.getItem(2);
            if (itemStack != null) {
                itemStack = craftManager.getItemStack(itemStack, player).clone();

                Map<Enchantment, Integer> enchantments = itemStack.getEnchantments();
                enchantments.forEach(itemStack::addUnsafeEnchantment);

                Craft craft = craftManager.getCraft(itemStack);
                ItemLoreBuilder.of(itemStack)
                        .craftType(craft.getType())
                        .damage(craftManager.getDamage(itemStack))
                        .description(craft.getLore())
                        .enchantments()
                        .applyLore();
            }
        }
    }
}
