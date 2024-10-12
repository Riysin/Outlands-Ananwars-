package me.orange.anan.npc.outlandsnpc;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.npc.outlandsnpc.loot.LootNPC;
import me.orange.anan.npc.outlandsnpc.merchant.FisherMerchantNPC;
import me.orange.anan.npc.outlandsnpc.task.FisherTaskNPC;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class OutlandsNPCRegistry {
    private final Map<String, OutlandsNPC> npcs = new HashMap<>();

    public OutlandsNPCRegistry(LootNPC LootNPC, FisherMerchantNPC fisherMerchantNPC, FisherTaskNPC fIsherTaskNPC) {
        registerNPC(LootNPC);
        registerNPC(fisherMerchantNPC);
        registerNPC(fIsherTaskNPC);
    }

    public void registerNPC(OutlandsNPC npc) {
        npcs.put(npc.getId(), npc);
    }

    public OutlandsNPC getNPC(String id) {
        return npcs.get(id);
    }

    public Collection<OutlandsNPC> getNPCs() {
        return npcs.values();
    }
}
