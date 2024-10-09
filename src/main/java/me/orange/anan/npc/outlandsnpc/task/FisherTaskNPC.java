package me.orange.anan.npc.outlandsnpc.task;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.tablist.util.Skin;
import me.orange.anan.npc.NPCType;
import me.orange.anan.npc.OutlandsNPC;

@InjectableComponent
public class FisherTaskNPC implements OutlandsNPC {
    @Override
    public String getID() {
        return "task.fisher";
    }

    @Override
    public String getName() {
        return "FisherMan";
    }

    @Override
    public Skin getSkin() {
        return new Skin("task", "task");
    }

    @Override
    public NPCType getType() {
        return NPCType.TASK;
    }
}
