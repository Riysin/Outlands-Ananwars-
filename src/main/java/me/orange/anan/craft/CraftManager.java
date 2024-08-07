package me.orange.anan.craft;

import io.fairyproject.bukkit.listener.ListenerRegistry;
import io.fairyproject.bukkit.nbt.NBTKey;
import io.fairyproject.bukkit.nbt.NBTModifier;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.FairyItemRegistry;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.bukkit.util.items.behaviour.ItemBehaviour;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.log.Log;
import me.orange.anan.craft.behaviour.BehaviourManager;
import me.orange.anan.craft.behaviour.CraftBehaviour;
import me.orange.anan.craft.config.CraftConfig;
import me.orange.anan.craft.config.CraftElement;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class CraftManager {
    private Map<String, Craft> crafts = new LinkedHashMap<>();
    private final CraftConfig config;
    private final BehaviourManager behaviourManager;
    private final FairyItemRegistry fairyItemRegistry;

    public Map<String, Craft> getCrafts() {
        return crafts;
    }

    public CraftManager(CraftConfig config, BehaviourManager behaviourManager, FairyItemRegistry fairyItemRegistry) {
        this.config = config;
        this.behaviourManager = behaviourManager;
        this.fairyItemRegistry = fairyItemRegistry;
        loadConfigFile();
    }

    public void loadConfigFile(){
        config.loadAndSave();
        config.getCraftTypes().forEach(element -> {
            element.getCrafts().forEach(craftElement -> {

                Craft craft = new Craft();
                craft.setType(element.getCraftType());
                craft.setMenuIcon(craftElement.getIcon());
                craft.setTier(craftElement.getTier());
                craft.setTime(craftElement.getTime());
                craft.setItemStack(
                        ItemBuilder.of(craftElement.getMaterial())
                                .name(craftElement.getDisplayName())
                                .lore(craftElement.getLore())
                                .tag(NBTKey.create("craft"), craftElement.getId())
                                .build()
                );

                Map<String, Integer> recipes = new HashMap<>(craftElement.getRecipes());
                craft.setRecipe(recipes);
                crafts.put(craftElement.getId(), craft);
            });
        });

        crafts.forEach((id, craft) -> {
            if(fairyItemRegistry.has(id))
                fairyItemRegistry.unregister(fairyItemRegistry.get(id));
            createFairyItem(craft);
        });
    }

    public ItemStack getConfigItemWithID(String ID) {
        AtomicReference<ItemStack> item = new AtomicReference<>();

        config.getCraftTypes().forEach(element -> {
            element.getCrafts().forEach(craftElement -> {
                if (craftElement.getId().equals(ID)) {
                    item.set(ItemBuilder.of(craftElement.getMaterial())
                            .name(craftElement.getDisplayName())
                            .lore(craftElement.getLore())
                            .tag(NBTKey.create("craft"), craftElement.getId())
                            .build());
                }
            });
        });

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
        for (ItemStack requiredItem : getRecipesFromIDs(craft.getRecipe(), player)) {
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
        for (ItemStack requiredItem : getRecipesFromIDs(craft.getRecipe(), player)) {
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

    public ItemStack getItemStack(Craft craft, Player player){
        return fairyItemRegistry.get(craft.getID()).provideItemStack(player);
    }

    private void createFairyItem(Craft craft){
        ItemStack itemStack = craft.getItemStack();
        Map<String, CraftBehaviour> behaviours = behaviourManager.getBehaviours();

        FairyItem.Builder builder = FairyItem.builder(craft.getID())
                .item(itemStack);

        if(behaviours.containsKey(craft.getID()))
            for (ItemBehaviour behaviour : behaviours.get(craft.getID()).getBehaviours()) {
                builder.behaviour(behaviour);
            }

        builder.create(fairyItemRegistry);
    }

    public List<ItemStack> getRecipesFromIDs(Map<String, Integer> recipes, Player player){
        List<ItemStack> items = new ArrayList<>();
        recipes.forEach((id, amount) -> {
            ItemStack item = fairyItemRegistry.get(id).provideItemStack(player);
            item.setAmount(amount);
            items.add(item);
        });

        return items;
    }

    public void removeItemsFromInventory(Player player, ItemStack itemStack, int count) {
        int amountToRemove = itemStack.getAmount() * count;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.isSimilar(itemStack)) {
                int itemAmount = item.getAmount();
                if (itemAmount > amountToRemove) {
                    item.setAmount(itemAmount - amountToRemove);
                    break;
                } else {
                    player.getInventory().remove(item);
                    amountToRemove -= itemAmount;
                    if (amountToRemove <= 0) {
                        break;
                    }
                }
            }
        }
    }
}
