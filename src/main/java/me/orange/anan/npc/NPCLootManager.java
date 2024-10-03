package me.orange.anan.npc;

import io.fairyproject.container.InjectableComponent;

import me.orange.anan.craft.CraftManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

@InjectableComponent
public class NPCLootManager {
    private final CraftManager craftManager;

    public NPCLootManager(CraftManager craftManager) {
        this.craftManager = craftManager;
    }

    public ItemStack getLoot(Player player, Block block) {
        ItemStack loot = craftManager.getItemStack("emerald", player).clone();
        Random random = new Random();
        int chance = random.nextInt(100);

        if (chance < 10) {
            loot.setAmount(3);
        } else if (chance < 50) {
            loot.setAmount(2);
        }
        return loot;
    }
}
