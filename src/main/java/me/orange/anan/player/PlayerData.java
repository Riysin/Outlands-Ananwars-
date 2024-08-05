package me.orange.anan.player;

import io.fairyproject.mc.tablist.util.Skin;

import java.util.HashSet;
import java.util.Set;

public class PlayerData {
    private int kills = 0;
    private int deaths = 0;
    private boolean knocked = false;
    private boolean saving = false;
    private Skin skin = Skin.GRAY;
    private Set<String> canCraftItems = new HashSet<>();

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public Set<String> getCanCraftItems() {
        return canCraftItems;
    }

    public void setCanCraftItems(Set<String> canCraftItems) {
        this.canCraftItems = canCraftItems;
    }

    public boolean isSaving() {
        return saving;
    }

    public void setSaving(boolean saving) {
        this.saving = saving;
    }

    public boolean isKnocked() {
        return knocked;
    }

    public void setKnocked(boolean knocked) {
        this.knocked = knocked;
    }


    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

}
