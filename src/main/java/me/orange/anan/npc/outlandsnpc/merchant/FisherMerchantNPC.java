package me.orange.anan.npc.outlandsnpc.merchant;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.tablist.util.Skin;
import me.orange.anan.npc.NPCType;
import me.orange.anan.npc.OutlandsNPC;

@InjectableComponent
public class FisherMerchantNPC implements OutlandsNPC {
    @Override
    public String getID() {
        return "merchant.fisher";
    }

    @Override
    public String getName() {
        return "OldFisher";
    }

    @Override
    public Skin getSkin() {
        return new Skin("oldfisher", "oldfisher");
    }

    @Override
    public NPCType getType() {
        return NPCType.MERCHANT;
    }
}
