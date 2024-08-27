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
        return "Fisher";
    }

    @Override
    public String getDescription() {
        return "Fisher is a job that catches fish.";
    }

    @Override
    public XMaterial getIcon() {
        return null;
    }

    @Override
    public double calculateEarning() {
        return 0;
    }

    @Override
    public String getSkill1Name() {
        return "";
    }

    @Override
    public String getSkill1Description() {
        return "";
    }

    @Override
    public void skill1() {

    }

    @Override
    public String getSkill2Name() {
        return "";
    }

    @Override
    public String getSkill2Description() {
        return "";
    }

    @Override
    public void skill2() {

    }

    @Override
    public String getSkill3Name() {
        return "";
    }

    @Override
    public String getSkill3Description() {
        return "";
    }

    @Override
    public void skill3() {

    }

    @Override
    public String getActiveName() {
        return "";
    }

    @Override
    public String getActiveDescription() {
        return "";
    }

    @Override
    public XMaterial getActiveIcon() {
        return null;
    }

    @Override
    public void active() {

    }
}
