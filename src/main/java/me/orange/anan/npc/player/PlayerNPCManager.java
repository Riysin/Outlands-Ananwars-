package me.orange.anan.npc.player;

import io.fairyproject.container.InjectableComponent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Inventory;
import net.citizensnpcs.api.trait.trait.Owner;
import net.citizensnpcs.trait.HologramTrait;
import net.citizensnpcs.trait.SitTrait;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
public class PlayerNPCManager {
    public void setUpNPC(Player player) {
        if (getPlayerNPC(player) == null) {
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, player.getName());
            npc.getOrAddTrait(Owner.class).setOwner(player.getUniqueId());
            npc.getOrAddTrait(HologramTrait.class).addLine("§c[Offline]");
            npc.getOrAddTrait(HologramTrait.class).setLineHeight(0.25);
            npc.setProtected(false);
        }
    }

    public Player getNPCOwner(NPC npc) {
        return Bukkit.getPlayer(npc.getTraitNullable(Owner.class).getOwnerId());
    }

    public OfflinePlayer getNPCOfflineOwner(NPC npc) {
        return Bukkit.getOfflinePlayer(npc.getTraitNullable(Owner.class).getOwnerId());
    }

    public NPC getPlayerNPC(Player player) {
        for (NPC npc : CitizensAPI.getNPCRegistry()) {
            if(npc.getTraitNullable(Owner.class) == null) {
                continue;
            }
            if (npc.hasTrait(Owner.class) && npc.getTraitNullable(Owner.class).getOwnerId().equals(player.getUniqueId())) {
                return npc;
            }
        }
        return null;
    }

    public NPC getPlayerNPC(OfflinePlayer player) {
        for (NPC npc : CitizensAPI.getNPCRegistry()) {
            if(npc.getTraitNullable(Owner.class) == null) {
                continue;
            }
            if (npc.hasTrait(Owner.class) && npc.getTraitNullable(Owner.class).getOwnerId().equals(player.getUniqueId())) {
                return npc;
            }
        }
        return null;
    }

    public void spawnNPC(Player player) {
        NPC npc = getPlayerNPC(player);
        if (npc != null) {
            npc.getOrAddTrait(SitTrait.class).setSitting(player.getLocation().add(0, -0.35, 0));
            npc.getOrAddTrait(Inventory.class).setContents(player.getInventory().getContents());
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, player.getInventory().getHelmet());
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, player.getInventory().getChestplate());
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, player.getInventory().getLeggings());
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.BOOTS, player.getInventory().getBoots());
            npc.spawn(player.getLocation());
        }
    }

    public void despawnNPC(Player player) {
        NPC npc = getPlayerNPC(player);
        if (npc != null) {
            ItemStack[] npcItems = npc.getOrAddTrait(Inventory.class).getContents();

            for (int i = 0; i < Math.min(npcItems.length, 36); i++) {
                player.getInventory().setItem(i, npcItems[i]);
            }

            player.getInventory().setHelmet(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.HELMET));
            player.getInventory().setChestplate(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.CHESTPLATE));
            player.getInventory().setLeggings(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.LEGGINGS));
            player.getInventory().setBoots(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.BOOTS));

            npc.despawn();
        }
    }

    public Inventory getTraitInventory(OfflinePlayer player) {
        NPC npc = getPlayerNPC(player);
        if (npc != null) {
            return npc.getOrAddTrait(Inventory.class);
        }
        return null;
    }

    public Inventory getTraitInventory(NPC npc) {
        return npc.getOrAddTrait(Inventory.class);
    }
}

