package me.orange.anan.world.resource;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;

public class Resource {
    private ResourceType type;
    private XMaterial material;
    private Location location;

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public XMaterial getMaterial() {
        return material;
    }

    public void setMaterial(XMaterial material) {
        this.material = material;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
