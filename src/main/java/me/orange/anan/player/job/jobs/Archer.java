package me.orange.anan.player.job.jobs;

import com.cryptomorin.xseries.XMaterial;
import me.orange.anan.player.job.Job;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

        return roll < chance;
    }

    @Override
    public String getSkill2Name() {
        return "獵物追蹤";
    }

    @Override
    public String getSkill2Description() {
        return "擊殺動物時掉落物數量增加，讓資源收集更高效。";
    }

    @Override
    public boolean skill2(Player player, int level) {
        return level >= 10; // 等級10以上才可啟動
    }

    @Override
    public String getSkill3Name() {
        return "精準射擊";
    }

    @Override
    public String getSkill3Description() {
        return "使用弓箭時，箭矢有機會造成額外傷害或貫穿目標。";
    }

    @Override
    public boolean skill3(Player player, int level) {
        Random random = new Random();
        int roll = random.nextInt(100);
        int chance = 15 + level * 3; // 基礎機率15%，每級增加3%

        return roll < chance;
    }

    @Override
    public String getActiveName() {
        return "追獵之箭";
    }

    @Override
    public String getActiveDescription() {
        return "發射一支強化箭矢，對目標造成大量傷害並降低移動速度。";
    }

    @Override
    public XMaterial getActiveIcon() {
        return XMaterial.TIPPED_ARROW; // 用來象徵強化箭矢的圖標
    }

    @Override
    public boolean active(Player player, int level) {
        return level >= 40; // 等級30以上才能啟用
    }
}
