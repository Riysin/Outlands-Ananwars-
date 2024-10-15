package me.orange.anan.npc;

import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.config.yaml.YamlConfiguration;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;
import me.orange.anan.util.ItemStackEncoder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class LootConfig extends YamlConfiguration {
    protected LootConfig(Anan plugin) {
        super(plugin.getDataFolder().resolve("loot.yml"));
        this.loadAndSave();
    }

    @ElementType(LootConfigElement.class)
    private List<LootConfigElement> loots = new ArrayList<>();

    public List<LootConfigElement> getLoots() {
        return loots;
    }

    public void addLoot(ItemStack itemStack) {
        String base64 = ItemStackEncoder.itemStackToBase64(itemStack);
        StringBuilder id = new StringBuilder(itemStack.getType().toString() + "*" + itemStack.getAmount() + ";");

        itemStack.getEnchantments().forEach((enchantment, integer) -> {
            id.append(enchantment.getName()).append(":").append(integer).append(";");
        });

        if(itemStack.getType().equals(Material.ENCHANTED_BOOK)){
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            meta.getStoredEnchants().forEach((enchantment, integer) -> {
                id.append(enchantment.getName()).append(":").append(integer).append(";");
            });
        }

        LootConfigElement lootConfigElement = new LootConfigElement();
        lootConfigElement.setId(id.toString());
        lootConfigElement.setItem(base64);
        loots.add(lootConfigElement);
        this.save();
    }
}
