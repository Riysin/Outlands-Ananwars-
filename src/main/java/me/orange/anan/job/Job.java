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
    boolean skill1(Player player);
    String getSkill2Name();
    String getSkill2Description();
    boolean skill2(Player player);
    String getSkill3Name();
    String getSkill3Description();
    boolean skill3(Player player);
    String getActiveName();
    String getActiveDescription();
    XMaterial getActiveIcon();
    void active(Player player);
}
