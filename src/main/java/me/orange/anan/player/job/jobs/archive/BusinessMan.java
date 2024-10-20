package me.orange.anan.player.job.jobs.archive;

import com.cryptomorin.xseries.XMaterial;
import me.orange.anan.player.job.Job;
import org.bukkit.entity.Player;

import java.util.Random;

public class BusinessMan implements Job {
    @Override
    public String getID() {
        return "businessman";
    }

    @Override
    public String getName() {
        return "商人";
    }

    @Override
    public String getDescription() {
        return "商人專精於交易和賺取金錢。";
    }

    @Override
    public String getSuffix() {
        return " §6[商]§f";
    }

    @Override
    public XMaterial getIcon() {
        return XMaterial.EMERALD; // 使用綠寶石作為商人的圖標
    }

    @Override
    public String getUpgradeName() {
        return "精明交易";
    }

    @Override
    public String getUpgradeDescription() {
        return "增加在交易時獲得更好交易條件的機率。";
    }

    @Override
    public double getChancePerLevel() {
        return 3.0; // 每級增加的機率
    }

    @Override
    public boolean upgradeSKill(Player player, int level) {
        Random random = new Random();
        int roll = random.nextInt(100);
        return roll < level * getChancePerLevel(); // 根據等級計算成功機率
    }

    @Override
    public String getSkill1Name() {
        return "議價高手";
    }

    @Override
    public String getSkill1Description() {
        return "交易時有更高的機率獲得額外金錢。";
    }

    @Override
    public boolean skill1(Player player, int level) {
        return level >= 5; // 等級達到5即可獲得該技能
    }

    @Override
    public String getSkill2Name() {
        return "市場洞察";
    }

    @Override
    public String getSkill2Description() {
        return "獲得市場的價格變化資訊。";
    }

    @Override
    public boolean skill2(Player player, int level) {
        return level >= 15; // 等級達到15即可獲得該技能
    }

    @Override
    public String getSkill3Name() {
        return "商業直覺";
    }

    @Override
    public String getSkill3Description() {
        return "在交易中獲得額外的經驗。";
    }

    @Override
    public boolean skill3(Player player, int level) {
        return level >= 25; // 等級達到25即可獲得該技能
    }

    @Override
    public String getActiveName() {
        return "商業風暴";
    }

    @Override
    public String getActiveDescription() {
        return "獲得短暫的金錢獎勵，並且能夠額外獲得商品。";
    }

    @Override
    public XMaterial getActiveIcon() {
        return XMaterial.GOLD_INGOT; // 使用金錠作為商業風暴的圖標
    }

    @Override
    public boolean active(Player player, int level) {
        return level >= 40; // 等級達到40才能使用該技能
    }
}
