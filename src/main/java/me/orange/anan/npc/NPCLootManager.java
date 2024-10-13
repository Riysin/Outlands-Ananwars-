package me.orange.anan.npc;

import io.fairyproject.container.InjectableComponent;

import me.orange.anan.craft.CraftManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@InjectableComponent
public class NPCLootManager {
    private final CraftManager craftManager;
    private final LootConfig lootConfig;
    private final List<Loot> lootList = new ArrayList<>();

    public NPCLootManager(CraftManager craftManager, LootConfig lootConfig) {
        this.craftManager = craftManager;
        this.lootConfig = lootConfig;

        loadConfig();
    }

    public void loadConfig() {
        lootConfig.getLoots().forEach(lootConfigElement -> {
            Loot loot = new Loot();
            loot.setId(lootConfigElement.getId());
            loot.setAmount(lootConfigElement.getAmount());
            loot.setWeight(lootConfigElement.getWeight());
            lootList.add(loot);
        });
    }

    public ItemStack getLoot(Player player, Block block) {
        List<Loot> possibleLoots = new ArrayList<>();
        possibleLoots.addAll(lootList);

        if (possibleLoots.isEmpty()) {
            return null;
        }

        int totalWeight = possibleLoots.stream().mapToInt(Loot::getWeight).sum();
        int random = new Random().nextInt(totalWeight);

        int weight = 0;
        for (Loot loot : possibleLoots) {
            weight += loot.getWeight();
            if (random < weight) {
                ItemStack item = craftManager.getItemStack(loot.getId(),player);
                item.setAmount(loot.getAmount());
                return item;
            }
        }
        return null;
    }
}
