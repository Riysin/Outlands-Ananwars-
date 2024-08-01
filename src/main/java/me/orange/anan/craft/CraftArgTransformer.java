package me.orange.anan.craft;

import io.fairyproject.command.CommandContext;
import io.fairyproject.command.exception.ArgTransformException;
import io.fairyproject.command.parameter.ArgTransformer;
import io.fairyproject.container.Autowired;
import io.fairyproject.container.InjectableComponent;

import java.util.List;
import java.util.stream.Collectors;

@InjectableComponent
public class CraftArgTransformer implements ArgTransformer<Craft> {
    @Autowired
    private CraftManager craftManager;

    @Override
    public Class[] type() {
        return new Class[]{Craft.class};
    }

    @Override
    public Craft transform(CommandContext commandContext, String craftId) throws ArgTransformException {
        Craft craft = craftManager.getCrafts().get(craftId);
        if (craft == null) {
            throw new ArgTransformException("craft not found: " + craftId);
        }
        return craft;
    }

    @Override
    public List<String> tabComplete(CommandContext commandContext, String source) throws ArgTransformException {
        return craftManager.getCrafts().keySet().stream()
                .filter(id -> id.startsWith(source))
                .collect(Collectors.toList());
    }
}
