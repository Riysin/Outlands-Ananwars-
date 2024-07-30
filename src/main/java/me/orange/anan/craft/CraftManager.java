package me.orange.anan.craft;

import io.fairyproject.bukkit.nbt.NBTKey;
import io.fairyproject.bukkit.nbt.NBTModifier;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.building.*;
import me.orange.anan.craft.tool.WoodenHoe;
import me.orange.anan.craft.usage.Anvil;
import me.orange.anan.craft.usage.ClassUpgrader;
import me.orange.anan.craft.usage.CraftingTable;
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
        //build
        registerCraft(new WoodPlank());
        registerCraft(new BuildLv1());
        registerCraft(new BuildLv2());
        registerCraft(new BuildLv3());
        registerCraft(new BuildLv4());
        registerCraft(new BuildLv5());
        registerCraft(new Chest());
        registerCraft(new IronDoor());
        registerCraft(new Ladder());
        registerCraft(new WoodDoor());
        registerCraft(new WoodFence());
        registerCraft(new WoodSlab());
        registerCraft(new WoodStair());
        registerCraft(new WoodTrapdoor());
        //tools
        registerCraft(new WoodenHoe());
        //usage
        registerCraft(new Anvil());
        registerCraft(new ClassUpgrader());
        registerCraft(new CraftingTable());
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

    public int getPlayerItemAmount(Player player,ItemStack itemStack){
        String requiredNBT = NBTModifier.get().getString(itemStack, NBTKey.create("resource"));
        int playerAmount = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && requiredNBT.equals(NBTModifier.get().getString(item, NBTKey.create("resource")))) {
                playerAmount += item.getAmount();
            }
        }
        return playerAmount;
    }
}
