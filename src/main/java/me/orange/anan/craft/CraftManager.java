package me.orange.anan.craft;

import io.fairyproject.bukkit.nbt.NBTKey;
import io.fairyproject.bukkit.nbt.NBTModifier;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.building.WoodPlank;
import me.orange.anan.craft.tool.WoodenPickaxe;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@InjectableComponent
public class CraftManager {
    private Map<String, Craft> crafts = new LinkedHashMap<>();

    public Map<String, Craft> getCrafts() {
        return crafts;
    }

    public CraftManager() {
        registerCraft(new WoodenPickaxe());
        registerCraft(new WoodPlank());
    }

    public void registerCraft(Craft item) {
        crafts.put(item.getID(), item);
    }

    public boolean canCraft(Player player, Craft craft) {
        Map<String, Integer> playerMaterials = new HashMap<>();

        // Count materials in the player's inventory
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                String NBTValue = NBTModifier.get().getString(item, NBTKey.create("resource"));
                int count = playerMaterials.getOrDefault(NBTValue, 0);
                playerMaterials.put(NBTValue, count + item.getAmount());
            }
        }

        // Check if player has enough materials for the recipe
        for (ItemStack requiredItem : craft.getRecipe()) {
            String NBTValue = NBTModifier.get().getString(requiredItem, NBTKey.create("resource"));
            int requiredAmount = requiredItem.getAmount();
            int playerAmount = playerMaterials.getOrDefault(NBTValue, 0);

            if (playerAmount < requiredAmount) {
                return false;
            }
        }

        return true;
    }

    public boolean hasEnough(Player player, ItemStack itemStack) {
        String requiredNBT = NBTModifier.get().getString(itemStack, NBTKey.create("resource"));
        int requiredAmount = itemStack.getAmount();

        int playerAmount = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && requiredNBT.equals(NBTModifier.get().getString(item, NBTKey.create("resource")))) {
                playerAmount += item.getAmount();
            }
        }

        return playerAmount >= requiredAmount;
    }
}
