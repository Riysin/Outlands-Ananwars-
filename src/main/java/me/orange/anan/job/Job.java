package me.orange.anan.job;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;

public interface Job {
    String getID();
    String getName();
    String getDescription();
    String getSuffix();
    XMaterial getIcon();

    String getUpgradeName();
    String getUpgradeDescription();
    boolean upgradeSKill(int level);
    int getChancePerLevel();
    String getSkill1Name();
    String getSkill1Description();
    String getSkill2Name();
    String getSkill2Description();
    String getSkill3Name();
    String getSkill3Description();
    String getActiveName();
    String getActiveDescription();
    XMaterial getActiveIcon();
    void active(Player player);
}
