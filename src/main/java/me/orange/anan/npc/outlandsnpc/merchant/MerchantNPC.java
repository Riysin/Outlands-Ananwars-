package me.orange.anan.npc.outlandsnpc.merchant;

import me.orange.anan.npc.NPCType;
import me.orange.anan.npc.outlandsnpc.OutlandsNPC;
import org.bukkit.entity.Player;
import teammt.villagerguiapi.classes.VillagerInventory;
import teammt.villagerguiapi.classes.VillagerTrade;

import java.util.List;

public abstract class MerchantNPC extends OutlandsNPC {
    public MerchantNPC(String id, String name, NPCType type) {
        super(id, name, type);
    }

    public abstract List<VillagerTrade> getTrades();

    public void openTrade(Player player) {
        VillagerInventory inventory = new VillagerInventory(getTrades(), player);
        inventory.setName(getName());
        inventory.open();
    }

}
