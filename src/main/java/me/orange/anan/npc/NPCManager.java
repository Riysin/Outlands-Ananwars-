package me.orange.anan.npc;

import io.fairyproject.container.InjectableComponent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.*;
import net.citizensnpcs.trait.text.Text;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@InjectableComponent
public class NPCManager {
    public void createNPC(Player player, OutlandsNPC outlandsNPC){
        if(outlandsNPC.getType() == NPCType.TASK){
            createTaskNPC(player, outlandsNPC);
        } else if(outlandsNPC.getType() == NPCType.MERCHANT){
            createMerchantNPC(player, outlandsNPC);
        } else if(outlandsNPC.getType() == NPCType.LOOT){
            createLootNPC(player.getLocation());
        }
    }

    public void createTaskNPC(Player player, OutlandsNPC outlandsNPC) {
        Location location = player.getLocation().clone();
        NPC npc = getTemplateNPC(outlandsNPC.getName());

        npc.getOrAddTrait(Text.class).toggleSpeechBubbles();
        npc.getOrAddTrait(Text.class).toggleTalkClose();
        npc.getOrAddTrait(Text.class).setRange(6);
        npc.getOrAddTrait(Text.class).add("&eHello, I'm a task NPC!");
        npc.getOrAddTrait(Text.class).add("&eI have a task for you.");
        npc.getOrAddTrait(Text.class).add("&eWould you like to accept it?");
        npc.getOrAddTrait(HologramTrait.class).addLine("§e[Quest] §f");

        npc.getOrAddTrait(SkinTrait.class).setSkinPersistent(outlandsNPC.getID(), outlandsNPC.getSkin().skinSignature, outlandsNPC.getSkin().skinValue);
        npc.getOrAddTrait(CommandTrait.class).addCommand(new CommandTrait.NPCCommandBuilder("anpc task " + outlandsNPC.getID(), CommandTrait.Hand.RIGHT)
                .player(true));

        npc.spawn(location);
    }

    public void createMerchantNPC(Player player, OutlandsNPC outlandsNPC) {
        NPC npc = getTemplateNPC(outlandsNPC.getName());

        npc.getOrAddTrait(HologramTrait.class).addLine("§e[Merchant] §f");
        npc.getOrAddTrait(HologramTrait.class).setLineHeight(0.26);
        npc.getOrAddTrait(Text.class).add("&eHello, I have a lot of items for sale!");
        npc.getOrAddTrait(SkinTrait.class).setSkinPersistent(outlandsNPC.getID(), outlandsNPC.getSkin().skinSignature, outlandsNPC.getSkin().skinValue);
        npc.getOrAddTrait(CommandTrait.class).addCommand(new CommandTrait.NPCCommandBuilder("anpc shop " + outlandsNPC.getID(), CommandTrait.Hand.RIGHT)
                .player(true));

        npc.spawn(player.getLocation());
    }

    public void createLootNPC(Location location) {
        location.setX(location.getBlockX() + 0.5);
        location.setZ(location.getBlockZ() + 0.5);
        location.setYaw(0);
        location.getWorld().getBlockAt(location).setTypeIdAndData(123, (byte) 0, true);

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.SLIME, "loot");
        npc.getOrAddTrait(SlimeSize.class).setSize(2);
        npc.getOrAddTrait(HologramTrait.class).addLine("§e[Hit]");
        npc.getOrAddTrait(CommandTrait.class).setHideErrorMessages(true);
        npc.getOrAddTrait(CommandTrait.class).addCommand(new CommandTrait.NPCCommandBuilder("anpc hurt " + npc.getId(), CommandTrait.Hand.LEFT)
                .player(true));

        npc.spawn(location);

        setUpLootNPC(npc); // need to set up the NPC after spawning for the LivingEntity to be available
    }

    public void setUpLootNPC(NPC npc) {
        npc.data().set(NPC.Metadata.NAMEPLATE_VISIBLE, false);
        if (npc.getEntity() instanceof LivingEntity) {
            LivingEntity entity = ((LivingEntity) npc.getEntity());
            entity.setMaxHealth(20);
            entity.setHealth(20);
            entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
        }
    }

    private NPC getTemplateNPC(String name) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);

        npc.getOrAddTrait(LookClose.class).setPerPlayer(false);
        npc.getOrAddTrait(LookClose.class).setRealisticLooking(true);
        npc.getOrAddTrait(LookClose.class).setRange(4);
        npc.getOrAddTrait(LookClose.class).lookClose(true);

        npc.getOrAddTrait(Text.class).toggleRealisticLooking();
        npc.getOrAddTrait(Text.class).setDelay(80);

        return npc;
    }
}
