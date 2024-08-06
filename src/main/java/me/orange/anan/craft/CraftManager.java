package me.orange.anan.craft;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.nbt.NBTKey;
import io.fairyproject.bukkit.nbt.NBTModifier;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.log.Log;
import me.orange.anan.craft.config.CraftConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class CraftManager {
    private Map<String, Craft> crafts = new LinkedHashMap<>();
    private final CraftConfig config;

    public Map<String, Craft> getCrafts() {
        return crafts;
    }

    public CraftManager(CraftConfig config) {
        this.config = config;
        loadConfigFile();
    }

    public void loadConfigFile(){
        config.loadAndSave();
        config.getCraftTypes().forEach((type, element) -> {
            element.getCrafts().forEach((id, craftElement) -> {
                Craft craft = new Craft();
                craft.setType(CraftType.valueOf(type));
                craft.setMenuIcon(craftElement.getIcon());
                craft.setTier(craftElement.getTier());
                craft.setTime(craftElement.getTime());
                craft.setItemStack(
                        ItemBuilder.of(craftElement.getMaterial())
                                .name(craftElement.getDisplayName())
                                .lore(craftElement.getLore())
                                .tag(NBTKey.create("craft"), id)
                                .build()
                );

                List<ItemStack> recipe = new ArrayList<>();
                craftElement.getRecipes().forEach((k, v) -> {
                    recipe.add(
                            ItemBuilder.of(getConfigItemWithID(k))
                                    .amount(v)
                                    .build()
                    );
                });
                craft.setRecipe(recipe);
                crafts.put(id, craft);
            });
        });
    }

    public ItemStack getConfigItemWithID(String ID) {
        AtomicReference<ItemStack> item = new AtomicReference<>();

        config.getCraftTypes().forEach((type, element) -> {
            element.getCrafts().forEach((id, craftElement) -> {
                if (id.equals(ID)) {
                    item.set(ItemBuilder.of(craftElement.getMaterial())
                            .name(craftElement.getDisplayName())
                            .lore(craftElement.getLore())
                            .tag(NBTKey.create("craft"), id)
                            .build());
                }
            });
        });

        Log.info(item.toString());
        return item.get();
    }

    public CraftElement getCraftElementWithID(String ID) {
        AtomicReference<CraftElement> craftElement = new AtomicReference<>();

        config.getCraftTypes().forEach(element -> {
            element.getCrafts().forEach(craft -> {
                if (craft.getId().equals(ID)) {
                    craftElement.set(craft);
                }
            });
        });

        return craftElement.get();
    }

    public boolean canCraft(Player player, Craft craft) {
        Map<String, Integer> playerMaterials = new HashMap<>();

        // Count materials in the player's inventory
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                String NBTValue = NBTModifier.get().getString(item, NBTKey.create("craft"));
                int count = playerMaterials.getOrDefault(NBTValue, 0);
                playerMaterials.put(NBTValue, count + item.getAmount());
            }
        }

        // Check if player has enough materials for the recipe
        for (ItemStack requiredItem : craft.getRecipe()) {
            String NBTValue = NBTModifier.get().getString(requiredItem, NBTKey.create("craft"));
            int requiredAmount = requiredItem.getAmount();
            int playerAmount = playerMaterials.getOrDefault(NBTValue, 0);

            if (playerAmount < requiredAmount) {
                return false;
            }
        }

        return true;
    }

    public boolean hasEnough(Player player, ItemStack itemStack) {
        String requiredNBT = NBTModifier.get().getString(itemStack, NBTKey.create("craft"));
        int requiredAmount = itemStack.getAmount();

        int playerAmount = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && requiredNBT.equals(NBTModifier.get().getString(item, NBTKey.create("craft")))) {
                playerAmount += item.getAmount();
            }
        }

        return playerAmount >= requiredAmount;
    }

    public int getPlayerItemAmount(Player player, ItemStack itemStack) {
        String requiredNBT = NBTModifier.get().getString(itemStack, NBTKey.create("craft"));
        int playerAmount = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && requiredNBT.equals(NBTModifier.get().getString(item, NBTKey.create("craft")))) {
                playerAmount += item.getAmount();
            }
        }
        return playerAmount;
    }

    public int getCanCraftAmount(Player player, Craft craft) {
        Map<String, Integer> playerMaterials = new HashMap<>();

        // Count materials in the player's inventory
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                String NBTValue = NBTModifier.get().getString(item, NBTKey.create("craft"));
                int count = playerMaterials.getOrDefault(NBTValue, 0);
                playerMaterials.put(NBTValue, count + item.getAmount());
            }
        }

        // Check if player has enough materials for the recipe
        int canCraftAmount = Integer.MAX_VALUE;
        for (ItemStack requiredItem : craft.getRecipe()) {
            String NBTValue = NBTModifier.get().getString(requiredItem, NBTKey.create("craft"));
            int requiredAmount = requiredItem.getAmount();
            int playerAmount = playerMaterials.getOrDefault(NBTValue, 0);

            if (playerAmount < requiredAmount) {
                return 0;
            }

            canCraftAmount = Math.min(canCraftAmount, playerAmount / requiredAmount);
        }

        return canCraftAmount;
    }
}
