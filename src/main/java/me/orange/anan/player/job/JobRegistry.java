package me.orange.anan.player.job;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.job.jobs.Archer;
import me.orange.anan.player.job.jobs.Fisher;
import me.orange.anan.player.job.jobs.Pickpocket;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class JobRegistry {
    private List<Job> jobs = new ArrayList<>();

    public JobRegistry() {
        registerJob(new Fisher());
        registerJob(new Archer());
        registerJob(new Pickpocket());
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void registerJob(Job job) {
        jobs.add(job);
    }
}
