package me.orange.anan.job;

import java.util.HashMap;
import java.util.Map;

public class JobStats {
    private Job currentJob;
    private Map<Job,Integer> jobLevelMap = new HashMap<>();

    public Job getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(Job currentJob) {
        this.currentJob = currentJob;
    }

    public Map<Job, Integer> getJobLevelMap() {
        return jobLevelMap;
    }

    public void setJobLevelMap(Map<Job, Integer> jobLevelMap) {
        this.jobLevelMap = jobLevelMap;
    }
}
