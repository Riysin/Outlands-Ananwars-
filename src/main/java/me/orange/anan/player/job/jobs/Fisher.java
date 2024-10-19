package me.orange.anan.player.job.jobs;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.orange.anan.player.job.Job;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Fisher implements Job {
    @Override
    public String getID() {
        return "fisher";
    }

    @Override
    public String getName() {
        return "漁夫";
    }

    @Override
    public String getDescription() {
        return "漁夫是一個捕魚的工作。";
    }

    @Override
    public String getSuffix() {
        return " §3[漁]§f";
    }

    @Override
    public XMaterial getIcon() {
        return XMaterial.COD;
    }

    @Override
    public String getUpgradeName() {
        return "雙重捕魚";
    }

    @Override
    public String getUpgradeDescription() {
        return "增加一次捕到兩條魚的機會。";
    }

    @Override
    public int getChancePerLevel() {
        return 3;
    }

    @Override
    public boolean upgradeSKill(Player player, int level) {
        Random random = new Random();
        int roll = random.nextInt(100);
        int chancePerLevel = 3;

        if (roll < level * chancePerLevel) {
            return true;
        }

        return false;
    }

    @Override
    public String getSkill1Name() {
        return "快速捕魚";
    }

    @Override
    public String getSkill1Description() {
        return "將手中釣竿提升一級魚餌等級。";
    }

    @Override
    public boolean skill1(Player player, int level) {
        return level >= 10;
    }

    @Override
    public String getSkill2Name() {
        return "海神之力";
    }

    @Override
    public String getSkill2Description() {
        return "在水中獲得水中呼吸及加速。";
    }

    @Override
    public boolean skill2(Player player, int level) {
        if (level < 20) {
            return false;
        }

        Material type = player.getLocation().getBlock().getType();
        return type.equals(Material.WATER) || type.equals(Material.STATIONARY_WATER);

    }

    @Override
    public String getSkill3Name() {
        return "致命魚鉤";
    }

    @Override
    public String getSkill3Description() {
        return "釣到人有30%機率造成1傷害。";
    }

    @Override
    public boolean skill3(Player player, int level) {
        if (level < 30) {
            return false;
        }
        Random random = new Random();
        int roll = random.nextInt(100);
        int chance = 30;

        return roll < chance;
    }

    @Override
    public String getActiveName() {
        return "釣者之力";
    }

    @Override
    public String getActiveDescription() {
        return "。";
    }

    @Override
    public XMaterial getActiveIcon() {
        return XMaterial.FISHING_ROD;
    }

    @Override
    public boolean active(Player player , int level) {
        return level == 40;
    }
}
