package me.orange.anan.player.death;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.bed.Bed;
import me.orange.anan.player.bed.BedManager;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class RespawnMenu {
    private final GuiFactory guiFactory;
    private final BedManager bedManager;

    public RespawnMenu(GuiFactory guiFactory, BedManager bedManager) {
        this.guiFactory = guiFactory;
        this.bedManager = bedManager;
    }

    public void open(Player player) {
        AtomicReference<Boolean> isManualClose = new AtomicReference<>(true);
        // Open bed menu
        Gui gui = guiFactory.create(Component.text("§f§lRespawn Menu"));
        NormalPane pane = Pane.normal(9, 3);

        if (!bedManager.getBeds(player).isEmpty()) {
            // Add bed menu items
            int i = 0;
            for (Bed bed : bedManager.getBeds(player)) {
                pane.setSlot(i, GuiSlot.of(ItemBuilder.of(XMaterial.RED_BED)
                        .name("§7" + bed.getBedName())
                        .lore("§7Click to respawn")
                        .build(), clicker -> {
                    player.teleport(bed.getLocation());
                    isManualClose.set(false);
                    clicker.closeInventory();
                    clicker.setGameMode(GameMode.SURVIVAL);
                }));
                i++;
            }
        }

        pane.setSlot(26, GuiSlot.of(ItemBuilder.of(XMaterial.REDSTONE_BLOCK)
                .name("§7Respawn")
                .build(), clicker -> {
            clicker.teleport(getRandomRespawnLocation(player.getLocation()));
            isManualClose.set(false);
            clicker.closeInventory();
            clicker.setGameMode(GameMode.SURVIVAL);
        }));

        pane.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE).build()));

        gui.onCloseCallback(player1 -> {
            if (isManualClose.get())
                gui.open(player1);
        });

        gui.addPane(pane);
        gui.open(player);
    }

    public Location getRandomRespawnLocation(Location location) {
        // Define the region's boundaries
        int xMin = -300, xMax = -200;
        int zMin = 25, zMax = 125;

        // Generate random coordinates within the defined region
        Random random = new Random();
        int x = xMin + random.nextInt(xMax - xMin + 1);
        int z = zMin + random.nextInt(zMax - zMin + 1);
        int y = location.getWorld().getHighestBlockYAt(x, z);

        return new Location(location.getWorld(), x, y, z);
    }
}
