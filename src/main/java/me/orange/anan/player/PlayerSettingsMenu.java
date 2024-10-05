package me.orange.anan.player;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.ProfileInputType;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.pane.mapping.PaneMapping;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@InjectableComponent
public class PlayerSettingsMenu {
    private final GuiFactory guiFactory;

    public PlayerSettingsMenu(GuiFactory guiFactory) {
        this.guiFactory = guiFactory;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("Settings"));
        NormalPane pane = Pane.normal(PaneMapping.rectangle(1, 1, 7, 1));
        NormalPane border = Pane.normal(PaneMapping.outline(0, 0, 9, 3));

        pane.setSlot(0, GuiSlot.of(ItemBuilder.of(XMaterial.PLAYER_HEAD)
                .transformItemStack(itemStack -> {
                    return XSkull.of(itemStack).profile(Profileable.of(ProfileInputType.BASE64, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjE1MWNmZmRhZjMwMzY3MzUzMWE3NjUxYjM2NjM3Y2FkOTEyYmE0ODU2NDMxNThlNTQ4ZDU5YjJlYWQ1MDExIn19fQ==")).apply();
                })
                .name("Language!")
                .lore("§cNot implemented yet.")
                .build()));
        //friend login notification on/off
        pane.setSlot(1, GuiSlot.of(ItemBuilder.of(XMaterial.PLAYER_HEAD)
                .skull(player)
                .name("Friend Login Notification")
                .lore("§cNot implemented yet.")
                .build()));

        border.fillEmptySlots(GuiSlot.of(XMaterial.GRAY_STAINED_GLASS_PANE));

        gui.addPane(pane);
        gui.addPane(border);
        gui.open(player);
    }
}
