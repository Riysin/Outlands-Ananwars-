package me.orange.anan.job;

import io.fairyproject.command.CommandContext;
import io.fairyproject.command.exception.ArgTransformException;
import io.fairyproject.command.parameter.ArgTransformer;
import io.fairyproject.container.Autowired;
import io.fairyproject.container.InjectableComponent;

import java.util.List;
import java.util.stream.Collectors;

@InjectableComponent
public class JobArgTransformer implements ArgTransformer<Job> {
    @Autowired
    private JobRegistry jobRegistry;

    @Override
    public Class[] type() {
        return new Class[]{Job.class};
    }

    @Override
    public Job transform(CommandContext commandContext, String source) throws ArgTransformException {
        Job job = jobRegistry.getJobs().stream()
                .filter(j -> j.getID().equals(source))
                .findFirst()
                .orElse(null);

        if (job == null) {
            throw new ArgTransformException("job not found: " + source);
        }
        return job;
    }

    @Override
    public List<String> tabComplete(CommandContext commandContext, String source) throws ArgTransformException {
        return jobRegistry.getJobs().stream()
                .map(Job::getID)
                .filter(id -> id.startsWith(source))
                .collect(Collectors.toList());
    }
}
