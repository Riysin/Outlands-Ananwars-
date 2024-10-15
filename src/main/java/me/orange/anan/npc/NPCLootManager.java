package me.orange.anan.npc;

import io.fairyproject.container.InjectableComponent;

import me.orange.anan.util.ItemStackEncoder;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@InjectableComponent
public class NPCLootManager {
    private final LootConfig lootConfig;
    private final List<Loot> lootList = new ArrayList<>();

    public NPCLootManager(LootConfig lootConfig) {
        this.lootConfig = lootConfig;

        loadConfig();
    }

    public void loadConfig() {
        lootConfig.getLoots().forEach(lootConfigElement -> {
            ItemStack itemStack = ItemStackEncoder.base64ToItemStack(lootConfigElement.getItem());

            Loot loot = new Loot(itemStack, lootConfigElement.getWeight());
            if (!lootList.contains(loot)){
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
        int random = new Random().nextInt(totalWeight);

        int weight = 0;
        for (Loot loot : possibleLoots) {
            weight += loot.getWeight();
            if (random < weight) {
                ItemStack item = loot.getItem().clone();
                return item;
            }
        }
        return null;
    }
}
