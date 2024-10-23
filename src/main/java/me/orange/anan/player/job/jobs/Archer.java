package me.orange.anan.player.job.jobs;

import com.cryptomorin.xseries.XMaterial;
import me.orange.anan.player.job.Job;
import org.bukkit.entity.Player;

import java.util.Random;

public class Archer implements Job {

    @Override
    public String getID() {
        return "archer";
    }

    @Override
    public String getName() {
        return "弓箭手";
    }

    @Override
    public String getDescription() {
        return "精通弓箭的遠程狩獵者，擁有回收箭矢和高效狩獵的能力。";
    }

    @Override
    public String getSuffix() {
        return " §2[弓]§f";
    }

    @Override
    public XMaterial getIcon() {
        return XMaterial.BOW; // 使用弓作為圖標
    }

    @Override
    public String getUpgradeName() {
        return "箭矢回收";
    }

    @Override
    public String getUpgradeDescription() {
        return "有機會自動回收射出的箭矢。";
    }

    @Override
    public double getChancePerLevel() {
        return 5.0; // 每級增加5%的回收機率
    }

    @Override
    public boolean upgradeSKill(Player player, int level) {
        Random random = new Random();
        int roll = random.nextInt(100);
        double chancePerLevel = getChancePerLevel();

        return roll < level * chancePerLevel;
    }

    @Override
    public String getSkill1Name() {
        return "稀有獵物獵手";
    }

    @Override
    public String getSkill1Description() {
        return "有機會從動物掉落物中獲得額外的綠寶石。";
    }

    @Override
    public boolean skill1(Player player, int level) {
        Random random = new Random();
        int roll = random.nextInt(100);
        int chance = 10 + level * 2; // 基礎機率10%，每級增加2%

        return roll < chance && level >= 10;
    }

    @Override
    public String getSkill2Name() {
        return "自然守護";
    }

    @Override
    public String getSkill2Description() {
        return "當弓箭手在森林或草原環境中不移動（如草地或樹林）時，每3秒恢復1點生命值。";
    }

    @Override
    public boolean skill2(Player player, int level) {
        return level >= 20;
    }

    @Override
    public String getSkill3Name() {
        return "獵物追蹤";
    }

    @Override
    public String getSkill3Description() {
        return "射中生物時，箭矢有10%機會將其緩速5秒。";
    }

    @Override
    public boolean skill3(Player player, int level) {
        Random random = new Random();
        int roll = random.nextInt(100);
        int chance = 10;

        return roll < chance && level >= 30;
    }

    @Override
    public String getActiveName() {
        return "獵人步法";
    }

    @Override
    public String getActiveDescription() {
        return "向前大躍步並且增加跑步速度，持續5秒。";
    }

    @Override
    public XMaterial getActiveIcon() {
        return XMaterial.ARROW; // 用來象徵強化箭矢的圖標
    }

    @Override
    public boolean active(Player player, int level) {
        return level >= 40;
    }
}
