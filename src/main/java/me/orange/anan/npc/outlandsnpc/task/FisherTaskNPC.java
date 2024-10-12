package me.orange.anan.npc.outlandsnpc.task;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.tablist.util.Skin;
import me.orange.anan.npc.NPCType;
import me.orange.anan.npc.outlandsnpc.OutlandsNPC;

import java.util.UUID;


@InjectableComponent
public class FisherTaskNPC extends OutlandsNPC {
    public FisherTaskNPC() {
        super("task.fisher", "Fisher Task", NPCType.TASK);
    }

    @Override
    public Skin getSkin() {
        try {
            return Skin.download(UUID.fromString("90e25fb3-c5ff-47f8-9ea5-79bf0e9db77c"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
