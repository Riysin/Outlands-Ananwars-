package me.orange.anan.craft;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.nbt.NBTKey;
import io.fairyproject.bukkit.nbt.NBTModifier;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.FairyItemRegistry;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.behaviour.BehaviourManager;
import me.orange.anan.craft.behaviour.CraftBehaviour;
import me.orange.anan.craft.config.CraftConfig;
import me.orange.anan.craft.config.ToolConfig;
import me.orange.anan.util.ItemLoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@InjectableComponent
public class CraftManager {
    private final Map<String, Craft> crafts = new ConcurrentHashMap<>();
    private final CraftConfig craftConfig;
    private final ToolConfig toolConfig;
    private final BehaviourManager behaviourManager;
    private final FairyItemRegistry fairyItemRegistry;

    public CraftManager(CraftConfig craftConfig, ToolConfig toolConfig, BehaviourManager behaviourManager, FairyItemRegistry fairyItemRegistry) {
        this.craftConfig = craftConfig;
        this.toolConfig = toolConfig;
        this.behaviourManager = behaviourManager;
        this.fairyItemRegistry = fairyItemRegistry;
        loadConfig();
    }

    public Map<String, Craft> getCrafts() {
        return this.crafts;
    }

    public void loadConfig() {
        getCrafts().forEach((s, craft) -> {
            if (craft.getType() == CraftType.TOOL && !toolConfig.getToolMap().containsKey(s))
                toolConfig.getToolMap().put(s, 1);
        });
        toolConfig.save();

        craftConfig.getConfigElements().forEach(element ->
                element.getCrafts().forEach(craftElement -> {
                    Craft craft = new Craft();
                    craft.setType(element.getCraftType());
                    craft.setMenuIcon(craftElement.getIcon());
                    craft.setTier(craftElement.getTier());
                    craft.setTime(craftElement.getTime());
                    craft.setLore(craftElement.getLore());
                    craft.setItemStack(ItemBuilder.of(craftElement.getMaterial())
                            .name(ChatColor.RESET + craftElement.getDisplayName())
                            .editMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_ENCHANTS))
                            .clearLore()
                            .lore(ItemLoreBuilder.of(craftElement.getMaterial())
                                    .setCraft(this, craft)
                                    .craftType()
                                    .description()
                                    .build())
                            .tag(NBTKey.create("craft"), craftElement.getId())
                            .build());

                    craft.setRecipe(new HashMap<>(craftElement.getRecipes()));
                    crafts.put(craftElement.getId(), craft);
                })
        );

        createFairyItems(crafts);
    }

    private void createFairyItems(Map<String, Craft> crafts) {
        crafts.forEach((id, craft) -> {
            if (fairyItemRegistry.has(id)) {
                fairyItemRegistry.unregister(fairyItemRegistry.get(id));
            }

            FairyItem.Builder builder = FairyItem.builder(craft.getID())
                    .item(craft.getItemStack());

            CraftBehaviour craftBehaviour = behaviourManager.getBehaviours().get(craft.getID());
            if (craftBehaviour != null) {
                craftBehaviour.getBehaviours().forEach(builder::behaviour);
            }

            builder.create(fairyItemRegistry);
        });
    }

    public ItemStack getRawItem(String ID) {
        return getCrafts().get(ID).getItemStack();
    }

    public ItemStack getItemStack(Craft craft, Player player) {
        return fairyItemRegistry.get(craft.getID()).provideItemStack(player);
    }

    public ItemStack getItemStack(ItemStack itemStack, Player player) {
        Craft craft = getCraft(itemStack);
        return fairyItemRegistry.get(craft.getID()).provideItemStack(player);
    }

    public ItemStack getItemStack(String ID, Player player) {
        return fairyItemRegistry.get(ID).provideItemStack(player);
    }

    public int getDamage(ItemStack itemStack) {
        if (itemStack == null || getCraft(itemStack) == null) {
            return 1;
        }
        return toolConfig.getToolMap().getOrDefault(getCraft(itemStack).getID(), 1);
    }

    private Map<String, Integer> getInventoryItems(Player player) {
        Map<String, Integer> playerMaterials = new HashMap<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                String NBTValue = NBTModifier.get().getString(item, NBTKey.create("craft"));
                playerMaterials.merge(NBTValue, item.getAmount(), Integer::sum);
            }
        }
        return playerMaterials;
    }

    public boolean canCraftItem(Player player, Craft craft) {
        Map<String, Integer> items = getInventoryItems(player);

        return craft.getRecipe().entrySet().stream()
                .allMatch(entry -> items.getOrDefault(entry.getKey(), 0) >= entry.getValue());
    }

    public List<ItemStack> getRecipeList(Map<String, Integer> recipes, Player player) {
        List<ItemStack> items = new ArrayList<>();
        recipes.forEach((id, amount) -> {
            ItemStack item = fairyItemRegistry.get(id).provideItemStack(player);
            item.setAmount(amount);
            items.add(item);
        });

        return items;
    }

    public boolean hasEnough(Player player, ItemStack itemStack) {
        String requiredNBT = NBTModifier.get().getString(itemStack, NBTKey.create("craft"));
        return countPlayerItemAmount(player, requiredNBT) >= itemStack.getAmount();
    }

    public int getPlayerItemAmount(Player player, ItemStack itemStack) {
        String requiredNBT = NBTModifier.get().getString(itemStack, NBTKey.create("craft"));
        return countPlayerItemAmount(player, requiredNBT);
    }

    public int getMaxCraftAmount(Player player, Craft craft) {
        Map<String, Integer> playerMaterials = getInventoryItems(player);

        return getRecipeList(craft.getRecipe(), player).stream()
                .mapToInt(requiredItem -> playerMaterials.getOrDefault(
                        NBTModifier.get().getString(requiredItem, NBTKey.create("craft")), 0
                ) / requiredItem.getAmount())
                .min()
                .orElse(0);
    }

    public void removeItemsFromInventory(Player player, ItemStack itemStack, int count) {
        int amountToRemove = itemStack.clone().getAmount() * count;

        int playerAmount = countPlayerItemAmount(player, itemStack);
        if (playerAmount < amountToRemove) {
            throw new IllegalArgumentException("Player does not have enough items to remove");
        } else {
            player.getInventory().removeItem(ItemBuilder.of(itemStack).amount(amountToRemove).build());
            player.updateInventory();
        }
    }

    private int countPlayerItemAmount(Player player, String requiredNBT) {
        return Arrays.stream(player.getInventory().getContents())
                .filter(item -> item != null && requiredNBT.equals(NBTModifier.get().getString(item, NBTKey.create("craft"))))
                .mapToInt(ItemStack::getAmount)
                .sum();
    }

    private int countPlayerItemAmount(Player player, ItemStack itemStack) {
        return Arrays.stream(player.getInventory().getContents())
                .filter(item -> item != null && item.isSimilar(itemStack))
                .mapToInt(ItemStack::getAmount)
                .sum();
    }

    public Craft getCraft(ItemStack itemStack) {
        return crafts.values().stream()
                .filter(c -> c.getMenuIcon() == XMaterial.matchXMaterial(itemStack))
                .findFirst()
                .orElse(null);
    }

    public Craft getCraft(XMaterial material) {
        return crafts.values().stream()
                .filter(c -> c.getMenuIcon() == material)
                .findFirst()
                .orElse(null);
    }

    public Craft getCraft(Block block) {
        return crafts.values().stream()
                .filter(c -> c.getMenuIcon().parseMaterial() == block.getType() && c.getMenuIcon().getData() == block.getData())
                .findFirst()
                .orElse(null);
    }
}
