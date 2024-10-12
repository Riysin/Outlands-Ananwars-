package me.orange.anan.npc.outlandsnpc.loot;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.tablist.util.Skin;
import me.orange.anan.npc.NPCType;
import me.orange.anan.npc.outlandsnpc.OutlandsNPC;

@InjectableComponent
public class LootNPC extends OutlandsNPC {
    public LootNPC() {
        super("loot", "Loot", NPCType.LOOT);
    }
}
