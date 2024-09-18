package me.orange.anan.npc;

import io.fairyproject.container.InjectableComponent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.*;
import net.citizensnpcs.trait.text.Text;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.time.Duration;

@InjectableComponent
public class NPCManager {

    public void setUpMerchantNPC(int id) {
        NPC npc = CitizensAPI.getNPCRegistry().getById(id);
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

    public void createTaskNPC(String taskID, Location location) {
        NPC npc = getTemplateNPC(taskID);

        npc.getOrAddTrait(Text.class).toggleSpeechBubbles();
        npc.getOrAddTrait(Text.class).toggleTalkClose();
        npc.getOrAddTrait(Text.class).setRange(6);
        npc.getOrAddTrait(Text.class).add("&eHello, I'm a task NPC!");
        npc.getOrAddTrait(Text.class).add("&eI have a task for you.");
        npc.getOrAddTrait(Text.class).add("&eWould you like to accept it?");


        npc.getOrAddTrait(CommandTrait.class).addCommand(new CommandTrait.NPCCommandBuilder("anpc task " + taskID, CommandTrait.Hand.RIGHT)
                .player(true)
                .addPerm("npc.admin"));

        npc.spawn(location);
    }

    public void createLootNPC(String name, Location location) {
        location.setX(location.getBlockX() + 0.5);
        location.setZ(location.getBlockZ() + 0.5);
        location.setYaw(0);
        location.getWorld().getBlockAt(location).setTypeIdAndData(123, (byte) 0, true);

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.SLIME, name);

        npc.getOrAddTrait(SlimeSize.class).setSize(2);
        npc.getOrAddTrait(HologramTrait.class).addLine("§e[Hit]");
        npc.getOrAddTrait(HologramTrait.class).setLineHeight(0.3);

        npc.getOrAddTrait(CommandTrait.class).addCommand(new CommandTrait.NPCCommandBuilder("anpc hurt " + npc.getId(), CommandTrait.Hand.LEFT)
                .player(true)
                .addPerm("npc.admin"));

        npc.spawn(location);

        if(npc.getEntity() instanceof LivingEntity) {
            ((LivingEntity) npc.getEntity()).setMaxHealth(10);
            ((LivingEntity) npc.getEntity()).setHealth(10);
        }
    }

    private NPC getTemplateNPC(String name) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);

        npc.getOrAddTrait(HologramTrait.class).addLine("§e[NPC] §f");
        npc.getOrAddTrait(HologramTrait.class).setLineHeight(0.26);

        npc.getOrAddTrait(LookClose.class).setPerPlayer(false);
        npc.getOrAddTrait(LookClose.class).setRealisticLooking(true);
        npc.getOrAddTrait(LookClose.class).setRange(4);
        npc.getOrAddTrait(LookClose.class).lookClose(true);

        npc.getOrAddTrait(Text.class).toggleRealisticLooking();
        npc.getOrAddTrait(Text.class).setDelay(80);

        return npc;
    }
}
