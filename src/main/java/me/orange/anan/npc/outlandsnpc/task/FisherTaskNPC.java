package me.orange.anan.npc.outlandsnpc.task;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.tablist.util.Skin;
import me.orange.anan.npc.NPCType;
import me.orange.anan.npc.OutlandsNPC;

import java.util.UUID;


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
        try {
            return Skin.download(UUID.fromString("90e25fb3-c5ff-47f8-9ea5-79bf0e9db77c"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NPCType getType() {
        return NPCType.TASK;
    }
}
