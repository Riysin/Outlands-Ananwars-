package me.orange.anan.npc.outlandsnpc.merchant;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.tablist.util.Skin;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.fishing.FishManager;
import me.orange.anan.npc.NPCType;
import org.bukkit.entity.Player;
import teammt.villagerguiapi.classes.VillagerTrade;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@InjectableComponent
public class FisherMerchantNPC extends MerchantNPC {
    private final CraftManager craftManager;
    private final FishManager fishManager;

    public FisherMerchantNPC(CraftManager craftManager, FishManager fishManager) {
        super("merchant.fisher", "Old Fisher", NPCType.MERCHANT);
        this.craftManager = craftManager;
        this.fishManager = fishManager;
    }

    @Override
    public Skin getSkin() {
        try {
            return Skin.download(UUID.fromString("90e25fb3-c5ff-47f8-9ea5-79bf0e9db77c"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<VillagerTrade> getTrades(Player player) {
        List<VillagerTrade> trades = new ArrayList<>();
        trades.add(new VillagerTrade(craftManager.getItemStack("cod", player), fishManager.getFishPriceEmerald("cod", player), 100));
        trades.add(new VillagerTrade(craftManager.getItemStack("salmon", player), fishManager.getFishPriceEmerald("salmon", player), 100));
        trades.add(new VillagerTrade(craftManager.getItemStack("pufferfish", player), fishManager.getFishPriceEmerald("pufferfish", player), 100));
        trades.add(new VillagerTrade(craftManager.getItemStack("tropicalFish", player), fishManager.getFishPriceEmerald("tropicalFish", player), 100));
        return trades;
    }
}