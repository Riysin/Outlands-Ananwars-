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
import me.orange.anan.craft.config.CraftConfigElement;
import me.orange.anan.craft.config.CraftElement;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class CraftManager {
    private final Map<String, Craft> crafts = new LinkedHashMap<>();
    private Map<String, Integer> toolDamage = new HashMap<>();
    private final CraftConfig config;
    private final BehaviourManager behaviourManager;
    private final FairyItemRegistry fairyItemRegistry;

    public CraftManager(CraftConfig config, BehaviourManager behaviourManager, FairyItemRegistry fairyItemRegistry) {
        this.config = config;
        this.behaviourManager = behaviourManager;
        this.fairyItemRegistry = fairyItemRegistry;
        loadConfigFile();
    }

    public Map<String, Craft> getCrafts() {
        return this.crafts;
    }

    public void loadConfigFile() {
        config.loadAndSave();
        config.getConfigElements().forEach(element ->
                element.getCrafts().forEach(craftElement -> {
                    Craft craft = new Craft();
                    craft.setType(element.getCraftType());
                    craft.setMenuIcon(craftElement.getIcon());
                    craft.setTier(craftElement.getTier());
                    craft.setTime(craftElement.getTime());
                    craft.setItemStack(ItemBuilder.of(craftElement.getMaterial())
                            .name(craftElement.getDisplayName())
                            .lore(craftElement.getLore())
                            .tag(NBTKey.create("craft"), craftElement.getId())
                            .build());

                    craft.setRecipe(new HashMap<>(craftElement.getRecipes()));
                    crafts.put(craftElement.getId(), craft);
                })
        );

        crafts.forEach((id, craft) -> {
            if (fairyItemRegistry.has(id)) {
                fairyItemRegistry.unregister(fairyItemRegistry.get(id));
            }
            createFairyItem(craft);
        });
    }

    public ItemStack getConfigItemWithID(String ID) {
        return config.getConfigElements().stream()
                .flatMap(element -> element.getCrafts().stream())
                .filter(craftElement -> craftElement.getId().equals(ID))
                .findFirst()
                .map(craftElement -> ItemBuilder.of(craftElement.getMaterial())
                        .name(craftElement.getDisplayName())
                        .lore(getLores(craftElement))
                        .tag(NBTKey.create("craft"), craftElement.getId())
                        .build())
                .orElse(null);
    }

    private CraftType getCraftType(String ID) {
        return config.getConfigElements().stream()
                .filter(element -> element.getCrafts().stream().anyMatch(craftElement -> craftElement.getId().equals(ID)))
                .findFirst()
                .map(CraftConfigElement::getCraftType)
                .orElse(null);
    }

    private List<String> getLores(CraftElement craft) {
        List<String> lores = new ArrayList<>();
        lores.add("§8" + getCraftType(craft.getId()));
        lores.add("");
        lores.addAll(craft.getLore());
        if(getCraftType(craft.getId()) == CraftType.TOOL) {
            lores.add("§7傷害: §6"+ getToolDamage(craft.getId()));
        }
        return lores;
    }

    public CraftElement getCraftElementWithID(String ID) {
        return config.getConfigElements().stream()
                .flatMap(element -> element.getCrafts().stream())
                .filter(craft -> craft.getId().equals(ID))
                .findFirst()
                .orElse(null);
    }

    public int getToolDamage(String ID) {
        return toolDamage.get(ID);
    }

    public void setToolDamage(Map<String, Integer> toolDamage) {
        this.toolDamage = toolDamage;
    }

    public boolean canCraft(Player player, Craft craft) {
        Map<String, Integer> playerMaterials = countPlayerMaterials(player);

        return getRecipesFromIDs(craft.getRecipe(), player).stream()
                .allMatch(requiredItem -> playerMaterials.getOrDefault(
                        NBTModifier.get().getString(requiredItem, NBTKey.create("craft")), 0
                ) >= requiredItem.getAmount());
    }

    public boolean hasEnough(Player player, ItemStack itemStack) {
        String requiredNBT = NBTModifier.get().getString(itemStack, NBTKey.create("craft"));
        return countPlayerItemAmount(player, requiredNBT) >= itemStack.getAmount();
    }

    public int getPlayerItemAmount(Player player, ItemStack itemStack) {
        String requiredNBT = NBTModifier.get().getString(itemStack, NBTKey.create("craft"));
        return countPlayerItemAmount(player, requiredNBT);
    }

    public int getCanCraftAmount(Player player, Craft craft) {
        Map<String, Integer> playerMaterials = countPlayerMaterials(player);

        return getRecipesFromIDs(craft.getRecipe(), player).stream()
                .mapToInt(requiredItem -> playerMaterials.getOrDefault(
                        NBTModifier.get().getString(requiredItem, NBTKey.create("craft")), 0
                ) / requiredItem.getAmount())
                .min()
                .orElse(0);
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

    private void createFairyItem(Craft craft) {
        FairyItem.Builder builder = FairyItem.builder(craft.getID())
                .item(craft.getItemStack());

        CraftBehaviour craftBehaviour = behaviourManager.getBehaviours().get(craft.getID());
        if (craftBehaviour != null) {
            craftBehaviour.getBehaviours().forEach(builder::behaviour);
        }

        builder.create(fairyItemRegistry);
    }

    public List<ItemStack> getRecipesFromIDs(Map<String, Integer> recipes, Player player) {
        List<ItemStack> items = new ArrayList<>();
        recipes.forEach((id, amount) -> {
            ItemStack item = fairyItemRegistry.get(id).provideItemStack(player);
            item.setAmount(amount);
            items.add(item);
        });

        return items;
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

    private Map<String, Integer> countPlayerMaterials(Player player) {
        Map<String, Integer> playerMaterials = new HashMap<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                String NBTValue = NBTModifier.get().getString(item, NBTKey.create("craft"));
                playerMaterials.merge(NBTValue, item.getAmount(), Integer::sum);
            }
        }
        return playerMaterials;
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
        AtomicReference<Craft> craft = new AtomicReference<>(null);
        crafts.forEach((id, c) -> {
            if (c.getMenuIcon() == XMaterial.matchXMaterial(itemStack)) {
                craft.set(c);
            }
        });
        return craft.get();
    }

    public Craft getCraft(XMaterial material) {
        AtomicReference<Craft> craft = new AtomicReference<>(null);
        crafts.forEach((id, c) -> {
            if (c.getMenuIcon() == material) {
                craft.set(c);
            }
        });
        return craft.get();
    }

    public Craft getCraft(Block block) {
        AtomicReference<Craft> craft = new AtomicReference<>(null);
        crafts.forEach((id, c) -> {
            if (c.getMenuIcon().parseMaterial() == block.getType() && c.getMenuIcon().getData() == block.getData()) {
                craft.set(c);
            }
        });
        return craft.get();
    }
}
