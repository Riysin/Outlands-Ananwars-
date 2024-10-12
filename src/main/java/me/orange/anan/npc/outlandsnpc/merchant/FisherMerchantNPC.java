package me.orange.anan.npc.outlandsnpc.merchant;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.tablist.util.Skin;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.fishing.FishManager;
import me.orange.anan.npc.NPCType;
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
    public List<VillagerTrade> getTrades() {
        List<VillagerTrade> trades = new ArrayList<>();
        trades.add(new VillagerTrade(craftManager.getConfigItemWithID("cod"),fishManager.getFishPriceEmerald("cod"),100));
        trades.add(new VillagerTrade(craftManager.getConfigItemWithID("salmon"),fishManager.getFishPriceEmerald("salmon"),100));
        trades.add(new VillagerTrade(craftManager.getConfigItemWithID("pufferfish"),fishManager.getFishPriceEmerald("pufferfish"),100));
        trades.add(new VillagerTrade(craftManager.getConfigItemWithID("tropicalFish"),fishManager.getFishPriceEmerald("tropicalFish"),100));
        return trades;
    }
}