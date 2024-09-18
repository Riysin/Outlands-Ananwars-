package me.orange.anan.player.job;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.job.jobs.Fisher;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class JobRegistry {
    private List<Job> jobs = new ArrayList<>();

    public JobRegistry() {
        registerJob(new Fisher());
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void registerJob(Job job) {
        jobs.add(job);
    }
}
