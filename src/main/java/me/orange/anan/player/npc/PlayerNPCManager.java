package me.orange.anan.player.npc;

import io.fairyproject.container.InjectableComponent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Inventory;
import net.citizensnpcs.api.trait.trait.Owner;
import net.citizensnpcs.trait.SitTrait;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
public class PlayerNPCManager {
    public void setUpNPC(Player player) {
        if (getPlayerNPC(player) == null) {
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, player.getName());
            npc.getOrAddTrait(Owner.class).setOwner(player.getUniqueId());
            npc.getOrAddTrait(SitTrait.class).setSitting(player.getLocation().add(0, -0.4, 0));
            npc.setProtected(false);
            Bukkit.broadcastMessage("NPC created for " + player.getName());
        }
    }

    public Player getNPCOwner(NPC npc) {
        return Bukkit.getPlayer(npc.getTraitNullable(Owner.class).getOwnerId());
    }

    public NPC getPlayerNPC(Player player) {
        for (NPCRegistry npcRegistry : CitizensAPI.getNPCRegistries()) {
            for (NPC npc : npcRegistry) {
                if (npc.getTraitNullable(Owner.class).getOwnerId().equals(player.getUniqueId())) {
                    return npc;
                }
            }
        }
        return null;
    }

    public void spawnNPC(Player player) {
        NPC npc = getPlayerNPC(player);
        if (npc != null) {
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
            npc.despawn();
            // Get the NPC's inventory
            ItemStack[] npcItems = npc.getOrAddTrait(Inventory.class).getContents();

            // Set the player's inventory, limiting to 36 items (player inventory size)
            for (int i = 0; i < Math.min(npcItems.length, 36); i++) {
                player.getInventory().setItem(i, npcItems[i]);
            }
        }
    }

    public Inventory getTraitInventory(Player player) {
        NPC npc = getPlayerNPC(player);
        if (npc != null) {
            return npc.getOrAddTrait(Inventory.class);
        }
        return null;
    }
}

