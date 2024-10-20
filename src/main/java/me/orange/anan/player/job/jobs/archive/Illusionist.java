package me.orange.anan.player.job.jobs.archive;

import com.cryptomorin.xseries.XMaterial;
import me.orange.anan.player.job.Job;
import org.bukkit.entity.Player;

import java.util.Random;

public class Illusionist implements Job {
    @Override
    public String getID() {
        return "illusionist";
    }

    @Override
    public String getName() {
        return "幻術師";
    }

    @Override
    public String getDescription() {
        return "幻術師擅長用幻影和迷惑技巧來迷惑敵人。";
    }

    @Override
    public String getSuffix() {
        return " §3[幻]§f";
    }

    @Override
    public XMaterial getIcon() {
        return XMaterial.ENDER_PEARL; // 使用末影珍珠作為圖標
    }

    @Override
    public String getUpgradeName() {
        return "幻影操控";
    }

    @Override
    public String getUpgradeDescription() {
        return "有機率在戰鬥中創造一個假的自己，讓敵人攻擊幻影。";
    }

    @Override
    public double getChancePerLevel() {
        return 2.0; // 每級增加2%的機率
    }

    @Override
    public boolean upgradeSKill(Player player, int level) {
        Random random = new Random();
        int roll = random.nextInt(100);
        double chancePerLevel = getChancePerLevel(); // 使用每級機率

        return roll < level * chancePerLevel;
    }

    @Override
    public String getSkill1Name() {
        return "迷惑光環";
    }

    @Override
    public String getSkill1Description() {
        return "敵人周圍的攻擊準確度下降。";
    }

    @Override
    public boolean skill1(Player player, int level) {
        return true; // 永遠有效，因為是被動技能
    }

    @Override
    public String getSkill2Name() {
        return "虛無屏障";
    }

    @Override
    public String getSkill2Description() {
        return "低血量時自動觸發護盾，阻擋下一次受到的傷害。";
    }

    @Override
    public boolean skill2(Player player, int level) {
        return true; // 永遠有效，因為是被動技能
    }

    @Override
    public String getSkill3Name() {
        return "幻覺反射";
    }

    @Override
    public String getSkill3Description() {
        return "受到傷害時，有機會將部分傷害反彈給敵人。";
    }

    @Override
    public boolean skill3(Player player, int level) {
        return true; // 永遠有效，因為是被動技能
    }

    @Override
    public String getActiveName() {
        return "虛影步";
    }

    @Override
    public String getActiveDescription() {
        return "瞬間移動到隨機位置，留下幻影迷惑敵人。";
    }

    @Override
    public XMaterial getActiveIcon() {
        return XMaterial.ENDER_PEARL; // 使用末影珍珠作為主動技能圖標
    }

    @Override
    public boolean active(Player player, int level) {
        return level >= 30; // 30級解鎖
    }
}
