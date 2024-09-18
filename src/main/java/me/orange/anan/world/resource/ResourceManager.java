package me.orange.anan.world.resource;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.npc.NPCManager;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@InjectableComponent
public class ResourceManager {
    private final NPCManager npcManager;
    private List<Resource> resources = new ArrayList<>();

    public ResourceManager(NPCManager npcManager) {
        this.npcManager = npcManager;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public Resource getResourceFromLocation(Block block) {
        for (Resource resource : resources) {
            if (resource.getLocation().equals(block.getLocation())) {
                return resource;
            }
        }
        return null;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public void addResource(Block block) {
        Resource resource = new Resource();
        Material type = block.getType();
        if (type.equals(Material.LOG) || type.equals(Material.LOG_2)) {
            resource.setType(ResourceType.TREE);
        } else if (type.equals(XMaterial.COAL_ORE.parseMaterial())
                || type.equals(XMaterial.IRON_ORE.parseMaterial())
                || type.equals(XMaterial.GOLD_ORE.parseMaterial())) {
            resource.setType(ResourceType.ORE);
        } else if (type.equals(XMaterial.REDSTONE_LAMP.parseMaterial())) {
            resource.setType(ResourceType.LOOT);
        } else {
            return;
        }
        resource.setMaterial(XMaterial.matchXMaterial(type));
        resource.setLocation(block.getLocation());
        resources.add(resource);
    }

    public void respawnOre(World world) {
        // Use an iterator to avoid ConcurrentModificationException
        Iterator<Resource> iterator = resources.iterator();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            if (resource.getType().equals(ResourceType.ORE)) {
                world.getBlockAt(resource.getLocation()).setType(resource.getMaterial().parseMaterial());
                iterator.remove(); // Safely remove the resource
            }
        }
    }

    public void respawnLoot(World world) {
        // Use an iterator to avoid ConcurrentModificationException
        Iterator<Resource> iterator = resources.iterator();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            if (resource.getType().equals(ResourceType.LOOT)) {
                resource.getLocation().setWorld(world);
                npcManager.createLootNPC("Loot", resource.getLocation());
                iterator.remove(); // Safely remove the resource
            }
        }
    }
}
