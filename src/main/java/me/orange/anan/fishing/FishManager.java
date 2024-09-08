package me.orange.anan.fishing;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.CraftManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;

@InjectableComponent
public class FishManager {
    private final CraftManager craftManager;
    private FishConfig fishConfig;
    private final Random random = new Random();

    public FishManager(CraftManager craftManager, FishConfig fishConfig) {
        this.craftManager = craftManager;
        this.fishConfig = fishConfig;
    }

    public FishConfig getFishConfig() {
        return fishConfig;
    }

    public void setFishConfig(FishConfig fishConfig) {
        this.fishConfig = fishConfig;
    }

    public ItemStack getFishingLoot(Player player, int amount) {
        ItemStack fish = getRandomFish(player);
        ItemStack loot = ItemBuilder.of(fish).clone().amount(amount).build();
        return loot;
    }

    public ItemStack getRandomFish(Player player) {
        int totalWeight = 0;
        for (FishConfigElement element : fishConfig.getFishMap().values()) {
            totalWeight += element.getWeight();
        }

        int randomValue = random.nextInt(totalWeight);

        for (String fish : fishConfig.getFishMap().keySet()) {
            FishConfigElement element = fishConfig.getFishMap().get(fish);
            if (randomValue < element.getWeight()) {
                return craftManager.getItemStack(fish, player);
            }
            randomValue -= element.getWeight();
        }
        return ItemBuilder.of(XMaterial.COD).name("§f§lno way").build();
    }
}
