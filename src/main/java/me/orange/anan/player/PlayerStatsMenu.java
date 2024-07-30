package me.orange.anan.player;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.config.PlayerConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@InjectableComponent
public class PlayerStatsMenu {
    private final GuiFactory guiFactory;
    private final PlayerConfig playerConfig;

    public PlayerStatsMenu(GuiFactory guiFactory, PlayerDataManager playerDataManager, PlayerConfig playerConfig) {
        this.guiFactory = guiFactory;
        this.playerConfig = playerConfig;
    }

    public void open(Player ctx, Player player) {
        Gui gui = guiFactory.create(Component.text("My Stats"));

        NormalPane pane = Pane.normal(9, 3);
        pane.setSlot(13, GuiSlot.of(ItemBuilder
                .of(XMaterial.PLAYER_HEAD)
                .skull(player.getName())
                .name(player.getDisplayName())
                .lore("擊殺數: " + playerConfig.getPlayerElement(ctx.getPlayer().getName()).getKills()
                        , "死亡數: " + playerConfig.getPlayerElement(ctx.getPlayer().getName()).getDeaths())
                .build()));
        pane.fillEmptySlots(GuiSlot.of(XMaterial.GRAY_STAINED_GLASS_PANE));

        gui.addPane(pane);

        gui.open(ctx);
    }
}
