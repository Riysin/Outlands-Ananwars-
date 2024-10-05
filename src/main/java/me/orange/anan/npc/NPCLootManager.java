package me.orange.anan.npc;

import io.fairyproject.container.InjectableComponent;

import me.orange.anan.craft.CraftManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

@InjectableComponent
public class NPCLootManager {
    private final CraftManager craftManager;

    public NPCLootManager(CraftManager craftManager) {
        this.craftManager = craftManager;

        invisibleSlime();
    }

    public void invisibleSlime(){
        Bukkit.getWorlds().forEach(world -> {
            world.getEntities().forEach(entity -> {
                if (entity.hasMetadata("NPC") && entity.getType().equals(EntityType.SLIME) && entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    if (!livingEntity.hasPotionEffect(PotionEffectType.INVISIBILITY)){
                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
                    }
                }
            });
        });
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
