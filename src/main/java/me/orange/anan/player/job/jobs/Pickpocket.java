package me.orange.anan.player.job.jobs;

import com.cryptomorin.xseries.XMaterial;
import me.orange.anan.player.job.Job;
import org.bukkit.entity.Player;

import java.util.Random;

public class Pickpocket implements Job {

    @Override
    public String getID() {
        return "pickpocket";
    }

    @Override
    public String getName() {
        return "扒手";
    }

    @Override
    public String getDescription() {
        return "擅長從敵對生物或玩家身上竊取物品。";
    }

    @Override
    public String getSuffix() {
        return " §6[扒]§f";
    }

    @Override
    public XMaterial getIcon() {
        return XMaterial.CHEST; // 象徵竊取的圖標
    }

    @Override
    public String getUpgradeName() {
        return "快速竊取";
    }

    @Override
    public String getUpgradeDescription() {
        return "增加竊取成功率，每級提升2.5%。";
    }

    @Override
    public double getChancePerLevel() {
        return 2.5;
    }

    @Override
    public boolean upgradeSKill(Player player, int level) {
        Random random = new Random();
        int roll = random.nextInt(100);
        double chancePerLevel = 2.5; // 每升級提升竊取成功率

        return roll < level * chancePerLevel;
    }

    @Override
    public String getSkill1Name() {
        return "輕巧手法";
    }

    @Override
    public String getSkill1Description() {
        return "交易時有機會偷取少量的物品或綠寶石。";
    }

    @Override
    public boolean skill1(Player player, int level) {
        return level >= 10;
    }

    @Override
    public String getSkill2Name() {
        return "靈活身手";
    }

    @Override
    public String getSkill2Description() {
        return "移動速度提高5%，並在短時間內無法被鎖定。";
    }

    @Override
    public boolean skill2(Player player, int level) {
        return level >= 20;
    }

    @Override
    public String getSkill3Name() {
        return "精準偷襲";
    }

    @Override
    public String getSkill3Description() {
        return "背後攻擊有額外傷害，並有15%機率暈眩敵人。";
    }

    @Override
    public boolean skill3(Player player, int level) {
        if (level < 30) {
            return false;
        }
        Random random = new Random();
        int roll = random.nextInt(100);
        return roll < 15; // 15%的機率讓敵人暈眩
    }

    @Override
    public String getActiveName() {
        return "神出鬼沒";
    }

    @Override
    public String getActiveDescription() {
        return "短時間內進入隱身狀態，可以無聲接近敵人並進行強化偷襲。";
    }

    @Override
    public XMaterial getActiveIcon() {
        return XMaterial.ENDER_PEARL; // 用來象徵隱身的圖標
    }

    @Override
    public boolean active(Player player, int level) {
        return level == 40;
    }
}
