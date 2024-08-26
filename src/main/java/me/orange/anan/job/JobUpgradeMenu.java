package me.orange.anan.job;

import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.container.InjectableComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@InjectableComponent
public class JobUpgradeMenu {
    private final GuiFactory guiFactory;

    public JobUpgradeMenu(GuiFactory guiFactory) {
        this.guiFactory = guiFactory;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("Job Upgrade Menu"));
        NormalPane pane = Pane.normal(9, 6);



        gui.addPane(pane);
        gui.open(player);
    }
}
