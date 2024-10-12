package me.orange.anan.npc.outlandsnpc;

import io.fairyproject.mc.tablist.util.Skin;
import me.orange.anan.npc.NPCType;

public abstract class OutlandsNPC {
    private String id;
    private String name;
    private NPCType type;

    public OutlandsNPC(String id, String name, NPCType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NPCType getType() {
        return type;
    }

    public void setType(NPCType type) {
        this.type = type;
    }

    public Skin getSkin() {
        return Skin.GRAY;
    }
}
