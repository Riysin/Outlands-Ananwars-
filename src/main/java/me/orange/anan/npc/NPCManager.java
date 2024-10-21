package me.orange.anan.npc;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.npc.outlandsnpc.OutlandsNPC;
import me.orange.anan.npc.outlandsnpc.merchant.MerchantNPC;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.*;
import net.citizensnpcs.trait.text.Text;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;

@InjectableComponent
public class NPCManager {
    public void createNPC(Player player, OutlandsNPC outlandsNPC){
        Location location = getTargetBlockLocation(player);
        if(outlandsNPC.getType() == NPCType.TASK){
            createTaskNPC(player, outlandsNPC);
        } else if(outlandsNPC.getType() == NPCType.MERCHANT){
            createMerchantNPC(player, outlandsNPC);
        } else if(outlandsNPC.getType() == NPCType.LOOT){
            createLootNPC(location);
        }
    }

    private Location getTargetBlockLocation(Player player){
        Set<Material> materials = new HashSet<>();
        materials.add(XMaterial.AIR.parseMaterial());
        materials.add(XMaterial.WATER.parseMaterial());
        materials.add(XMaterial.LAVA.parseMaterial());

        return player.getTargetBlock(materials, 5).getLocation().add(0,1,0);
    }

    public void createTaskNPC(Player player, OutlandsNPC outlandsNPC) {
        Location location = getTargetBlockLocation(player);
        NPC npc = getTemplateNPC(outlandsNPC.getName());

        npc.getOrAddTrait(Text.class).toggleSpeechBubbles();
        npc.getOrAddTrait(Text.class).toggleTalkClose();
        npc.getOrAddTrait(Text.class).setRange(6);
        npc.getOrAddTrait(Text.class).add("&eHello, I have a task for you.!");
        npc.getOrAddTrait(HologramTrait.class).addLine("§e[Quest] §f");

        npc.getOrAddTrait(SkinTrait.class).setSkinPersistent(outlandsNPC.getId(), outlandsNPC.getSkin().skinSignature, outlandsNPC.getSkin().skinValue);
        npc.getOrAddTrait(CommandTrait.class).addCommand(new CommandTrait.NPCCommandBuilder("anpc task " + outlandsNPC.getId(), CommandTrait.Hand.RIGHT)
                .player(true));

        npc.spawn(location);
    }

    public void createMerchantNPC(Player player, OutlandsNPC outlandsNPC) {
        Location location = getTargetBlockLocation(player);
        NPC npc = getTemplateNPC(outlandsNPC.getName());

        npc.getOrAddTrait(HologramTrait.class).addLine("§e[Merchant] §f");
        npc.getOrAddTrait(HologramTrait.class).setLineHeight(0.26);
        npc.getOrAddTrait(Text.class).add("&eHello, I have a lot of items for sale!");
        npc.getOrAddTrait(SkinTrait.class).setSkinPersistent(outlandsNPC.getId(), outlandsNPC.getSkin().skinSignature, outlandsNPC.getSkin().skinValue);
        npc.getOrAddTrait(CommandTrait.class).addCommand(new CommandTrait.NPCCommandBuilder("anpc shop " + outlandsNPC.getId(), CommandTrait.Hand.RIGHT)
                .player(true));

        npc.spawn(location);
    }

    public void openTrade(Player player, OutlandsNPC outlandsNPC){
        if(outlandsNPC.getType() == NPCType.MERCHANT){
            ((MerchantNPC) outlandsNPC).openTrade(player);
        }
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
