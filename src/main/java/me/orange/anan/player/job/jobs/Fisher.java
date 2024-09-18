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
        return " §3[F]§f" ;
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

    public boolean upgradeSKill(int level) {
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

    public boolean skill1(Player player) {
        ItemStack item = player.getItemInHand();
        return item.getType().equals(Material.FISHING_ROD);
    }

    @Override
    public String getSkill2Name() {
        return "捕魚狂熱";
    }

    @Override
    public String getSkill2Description() {
        return "有機會一次捕到兩條魚。";
    }

    public boolean skill2(Player player) {
        Random random = new Random();
        int roll = random.nextInt(100);
        int chance = 10;

        return roll < chance;
    }

    @Override
    public String getSkill3Name() {
        return "海神之力";
    }

    @Override
    public String getSkill3Description() {
        return "在水中擁有水中呼吸效果。";
    }

    public boolean skill3(Player player) {
        Material type = player.getLocation().getBlock().getType();
        return type.equals(Material.WATER) || type.equals(Material.STATIONARY_WATER);
    }

    @Override
    public String getActiveName() {
        return "漁王釣竿";
    }

    @Override
    public String getActiveDescription() {
        return "獲得一把超強的釣竿。";
    }

    @Override
    public XMaterial getActiveIcon() {
        return XMaterial.FISHING_ROD;
    }

    @Override
    public void active(Player player) {
        ItemStack item = ItemBuilder.of(XMaterial.FISHING_ROD)
                .name("§6漁王釣竿")
                .enchantment(XEnchantment.LURE,4)
                .enchantment(XEnchantment.LUCK_OF_THE_SEA,3)
                .build();

        item.getItemMeta().spigot().setUnbreakable(true);

        player.getInventory().addItem(item);
        player.sendMessage("§a你獲得了一把§6漁王釣竿§a!");
    }
}
