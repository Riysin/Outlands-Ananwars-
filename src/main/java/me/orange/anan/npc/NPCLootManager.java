package me.orange.anan.npc;

import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;

import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.util.ItemLoreBuilder;
import me.orange.anan.util.ItemStackEncoder;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@InjectableComponent
public class NPCLootManager {
    private final LootConfig lootConfig;
    private final CraftManager craftManager;
    private final List<Loot> lootList = new ArrayList<>();

    public NPCLootManager(LootConfig lootConfig, CraftManager craftManager) {
        this.lootConfig = lootConfig;
        this.craftManager = craftManager;

        loadConfig();
    }

    public void loadConfig() {
        lootConfig.getLoots().forEach(lootConfigElement -> {
            ItemStack itemStack = ItemStackEncoder.base64ToItemStack(lootConfigElement.getItem());
            Craft craft = craftManager.getCraft(itemStack);
            itemStack = ItemBuilder.of(itemStack)
                    .name(craft.getName())
                    .editMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_ENCHANTS))
                    .clearLore()
                    .lore(ItemLoreBuilder.of(itemStack)
                            .setCraft(craftManager, craft)
                            .damage()
                            .craftType()
                            .description()
                            .enchantments()
                            .build())
                    .build();

            Loot loot = new Loot(itemStack, lootConfigElement.getWeight());
            if (!lootList.contains(loot)) {
                lootList.add(loot);
            }
        });
    }

    public ItemStack getLoot(Player player, Block block) {
        List<Loot> possibleLoots = new ArrayList<>();
        possibleLoots.addAll(lootList);

        if (possibleLoots.isEmpty()) {
            return null;
        }

        int totalWeight = possibleLoots.stream().mapToInt(Loot::getWeight).sum();
        int random = new Random().nextInt(totalWeight) + 1;

        for (Loot loot : possibleLoots) {
            random -= loot.getWeight();
            if (random <= 0) {
                return loot.getItem().clone();
            }
        }
        return null;
    }
}
