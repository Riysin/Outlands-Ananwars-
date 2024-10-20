package me.orange.anan.player.job;

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
    boolean upgradeSKill(Player player,int level);
    double getChancePerLevel();
    String getSkill1Name();
    String getSkill1Description();
    boolean skill1(Player player, int level);
    String getSkill2Name();
    String getSkill2Description();
    boolean skill2(Player player, int level);
    String getSkill3Name();
    String getSkill3Description();
    boolean skill3(Player player, int level);
    String getActiveName();
    String getActiveDescription();
    XMaterial getActiveIcon();
    boolean active(Player player, int level);
}
