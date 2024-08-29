package me.orange.anan.job;

import java.util.HashMap;
import java.util.Map;

public class JobStats {
    private Job currentJob;
    private Map<String,Integer> jobLevelMap = new HashMap<>();

    public Job getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(Job currentJob) {
        this.currentJob = currentJob;
    }

    public Map<String, Integer> getJobLevelMap() {
        return jobLevelMap;
    }

    public void setJobLevelMap(Map<String, Integer> jobLevelMap) {
        this.jobLevelMap = jobLevelMap;
    }
}
