package me.orange.anan.job;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Job {
    String getID();
    String getName();
    String getDescription();
    String getSuffix();
    XMaterial getIcon();

    String getUpgradeName();
    String getUpgradeDescription();
    int getChancePerLevel();
    ItemStack upgradeSKill(ItemStack itemStack, Player player,int level);
    String getSkill1Name();
    String getSkill1Description();
    void skill1();
    String getSkill2Name();
    String getSkill2Description();
    void skill2();
    String getSkill3Name();
    String getSkill3Description();
    void skill3();
    String getActiveName();
    String getActiveDescription();
    XMaterial getActiveIcon();
    void active();
}
