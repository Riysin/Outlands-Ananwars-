package me.orange.anan.npc.outlandsnpc.loot;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.tablist.util.Skin;
import me.orange.anan.npc.NPCType;
import me.orange.anan.npc.OutlandsNPC;

@InjectableComponent
public class LootNPC implements OutlandsNPC {
    @Override
    public String getID() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Skin getSkin() {
        return null;
    }

    @Override
    public NPCType getType() {
        return null;
    }
}
