package me.orange.anan.job.jobs;

import com.cryptomorin.xseries.XMaterial;
import me.orange.anan.job.Job;

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
    public XMaterial getIcon() {
        return XMaterial.COD;
    }

    @Override
    public String getUpgradeName() {
        return "大師級漁夫";
    }

    @Override
    public String getUpgradeDescription() {
        return "增加捕捉稀有魚類的機率。";
    }

    @Override
    public double calculateEarning() {
        return 100; // 範例收益金額
    }

    @Override
    public String getSkill1Name() {
        return "快速捕魚";
    }

    @Override
    public String getSkill1Description() {
        return "能更快地捕魚。";
    }

    @Override
    public void skill1() {
        // 實現快速捕魚的邏輯
    }

    @Override
    public String getSkill2Name() {
        return "雙重捕魚";
    }

    @Override
    public String getSkill2Description() {
        return "有機會一次捕到兩條魚。";
    }

    @Override
    public void skill2() {
        // 實現雙重捕魚的邏輯
    }

    @Override
    public String getSkill3Name() {
        return "幸運魚鉤";
    }

    @Override
    public String getSkill3Description() {
        return "增加捕捉寶藏的機率。";
    }

    @Override
    public void skill3() {
        // 實現幸運魚鉤的邏輯
    }

    @Override
    public String getActiveName() {
        return "捕魚狂熱";
    }

    @Override
    public String getActiveDescription() {
        return "暫時提升捕魚速度。";
    }

    @Override
    public XMaterial getActiveIcon() {
        return XMaterial.FISHING_ROD;
    }

    @Override
    public void active() {
        // 實現捕魚狂熱的邏輯
    }
}
