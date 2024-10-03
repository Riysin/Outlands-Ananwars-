package me.orange.anan.npc;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import teammt.villagerguiapi.classes.VillagerInventory;
import teammt.villagerguiapi.classes.VillagerTrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@InjectableComponent
public class NPCShopManager {
    private Map<String, List<VillagerTrade>> trades = new HashMap<>();

    public NPCShopManager() {
        loadTrades();
    }

    private void loadTrades() {
        List<VillagerTrade> fisherTrades = new ArrayList<>();
        fisherTrades.add(new VillagerTrade(ItemBuilder.of(XMaterial.TROPICAL_FISH).build(), ItemBuilder.of(XMaterial.EMERALD).build(), 100));
        fisherTrades.add(new VillagerTrade(ItemBuilder.of(XMaterial.SALMON).build(), ItemBuilder.of(XMaterial.EMERALD).amount(10).build(), 100));
        this.trades.put("fisher", fisherTrades);
    }

    private Map<String, List<VillagerTrade>> getTrades() {
        return trades;
    }

    public void open(Player player, String merchantID) {
        List<VillagerTrade> trade = getTrades().get(merchantID);
        VillagerInventory inventory = new VillagerInventory(trade, player);

        inventory.setName(ChatColor.translateAlternateColorCodes('&', "&6" + merchantID));
        inventory.open();
    }
}
