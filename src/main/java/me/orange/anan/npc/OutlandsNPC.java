package me.orange.anan.npc;

import io.fairyproject.mc.tablist.util.Skin;

public interface OutlandsNPC {
    String getID();
    String getName();
    Skin getSkin();
    NPCType getType();
}
