package me.orange.anan.npc;

import io.fairyproject.container.InjectableComponent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.CommandTrait;
import net.citizensnpcs.trait.HologramTrait;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.text.Text;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

@InjectableComponent
public class NPCManager {
    public void setUpMerchantNPC(int id) {
        NPC npc = CitizensAPI.getNPCRegistry().getById(id);
        npc.setBukkitEntityType(EntityType.SLIME);
        npc.getStoredLocation().getWorld().getBlockAt(npc.getStoredLocation()).setTypeIdAndData(16, (byte) 0,true);
        getTemplateNPC("merchant").spawn(npc.getStoredLocation());
    }

    public void createNPC(String name, Location location) {
        NPC npc = getTemplateNPC(name);

        npc.getOrAddTrait(Text.class).add("&eHello, I'm an NPC!");
        npc.getOrAddTrait(CommandTrait.class).addCommand(new CommandTrait.NPCCommandBuilder("anpc say", CommandTrait.Hand.RIGHT)
                .player(true)
                .addPerm("npc.admin"));

        npc.spawn(location);
    }

    private NPC getTemplateNPC(String name) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        npc.getOrAddTrait(HologramTrait.class).addLine("§e[NPC]");
        npc.getOrAddTrait(HologramTrait.class).setLineHeight(0.25);

        npc.getOrAddTrait(LookClose.class).setPerPlayer(false);
        npc.getOrAddTrait(LookClose.class).setRealisticLooking(true);
        npc.getOrAddTrait(LookClose.class).setRange(4);
        npc.getOrAddTrait(LookClose.class).lookClose(true);

        npc.getOrAddTrait(Text.class).useRealisticLooking();
        npc.getOrAddTrait(Text.class).useSpeechBubbles();
        npc.getOrAddTrait(Text.class).setDelay(0);

        return npc;
    }
}
