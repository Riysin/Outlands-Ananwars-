package me.orange.anan.job;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.job.jobs.Fisher;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class JobRegister {
    private List<Job> jobs = new ArrayList<>();

    public JobRegister() {
        registerJob(new Fisher());
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void registerJob(Job job) {
        jobs.add(job);
    }
}
