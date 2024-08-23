package me.orange.anan.craft.behaviour.teamCore;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.config.CraftConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@InjectableComponent
public class TeamCoreMenu {
    private final GuiFactory guiFactory;
    private final TeamCoreManager teamCoreManager;

    public TeamCoreMenu(GuiFactory guiFactory, TeamCoreManager teamCoreManager, CraftConfig craftConfig) {
        this.guiFactory = guiFactory;
        this.teamCoreManager = teamCoreManager;
    }

    public void open(Player player, TeamCore teamCore) {
        Gui gui = guiFactory.create(Component.text("§f§l隊伍核心"));
        NormalPane pane = Pane.normal(9, 3);

        pane.setSlot(4,1, GuiSlot.of(ItemBuilder.of(XMaterial.BARRIER)
                .name("§c拆除")
                .lore("§7拆除隊伍核心")
                .build(), clicker -> {
            teamCore.getCoreCreeper().setHealth(0);
            teamCoreManager.removeTeamCore(teamCore);
            clicker.closeInventory();
        }));

        gui.addPane(pane);
        gui.open(player);
    }
}
