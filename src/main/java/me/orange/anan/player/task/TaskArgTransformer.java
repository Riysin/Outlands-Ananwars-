package me.orange.anan.player.task;

import io.fairyproject.command.CommandContext;
import io.fairyproject.command.exception.ArgTransformException;
import io.fairyproject.command.parameter.ArgTransformer;
import io.fairyproject.container.Autowired;
import io.fairyproject.container.InjectableComponent;

import java.util.List;
import java.util.stream.Collectors;

@InjectableComponent
public class TaskArgTransformer implements ArgTransformer<Task> {
    @Autowired
    private TaskRegistry taskRegistry;

    @Override
    public Class[] type() {
        return new Class[]{Task.class};
    }

    @Override
    public Task transform(CommandContext commandContext, String source) throws ArgTransformException {
        Task task = taskRegistry.getTasks().stream()
                .filter(t -> t.getId().equals(source))
                .findFirst()
                .orElse(null);

        if (task == null) {
            throw new ArgTransformException("task not found: " + source);
        }
        return task;
    }

    @Override
    public List<String> tabComplete(CommandContext commandContext, String source) throws ArgTransformException {
        return taskRegistry.getTasks().stream()
                .map(Task::getId)
                .filter(id -> id.startsWith(source))
                .collect(Collectors.toList());
    }
}


