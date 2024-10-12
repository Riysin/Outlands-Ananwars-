package me.orange.anan.npc.outlandsnpc;

import io.fairyproject.command.CommandContext;
import io.fairyproject.command.exception.ArgTransformException;
import io.fairyproject.command.parameter.ArgTransformer;
import io.fairyproject.container.Autowired;
import io.fairyproject.container.InjectableComponent;

import java.util.List;
import java.util.stream.Collectors;

@InjectableComponent
public class OutlandsNPCArgTransformer implements ArgTransformer<OutlandsNPC> {
    @Autowired
    private OutlandsNPCRegistry outlandsNPCRegistry;

    @Override
    public Class[] type() {
        return new Class[]{OutlandsNPC.class};
    }

    @Override
    public OutlandsNPC transform(CommandContext commandContext, String source) throws ArgTransformException {
        OutlandsNPC outlandsNPC = outlandsNPCRegistry.getNPCs().stream()
                .filter(npc -> npc.getId().equals(source))
                .findFirst()
                .orElse(null);

        if (outlandsNPC == null) {
            throw new ArgTransformException("npc not found: " + source);
        }
        return outlandsNPC;
    }

    @Override
    public List<String> tabComplete(CommandContext commandContext, String source) throws ArgTransformException {
        return outlandsNPCRegistry.getNPCs().stream()
                .map(OutlandsNPC::getId)
                .filter(id -> id.startsWith(source))
                .collect(Collectors.toList());
    }
}

