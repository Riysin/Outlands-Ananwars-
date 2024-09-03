package me.orange.anan.player.friend;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.pane.mapping.PaneMapping;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.PlayerDataManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@InjectableComponent
public class FriendMenu {
    private final GuiFactory guiFactory;
    private final PlayerDataManager playerDataManager;

    public FriendMenu(GuiFactory guiFactory, PlayerDataManager playerDataManager) {
        this.guiFactory = guiFactory;
        this.playerDataManager = playerDataManager;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("Friends"));
        NormalPane pane = Pane.normal(PaneMapping.rectangle(1, 1, 7, 3));
        NormalPane border = Pane.normal(PaneMapping.outline(0, 0, 9, 5));

        int slot = 0;

        gui.onDrawCallback($ -> {
            pane.clear();
            playerDataManager.getFriends(player).forEach(uuid -> {
                OfflinePlayer friend = Bukkit.getOfflinePlayer(uuid);

                pane.setSlot(slot, GuiSlot.of(ItemBuilder.of(XMaterial.PLAYER_HEAD)
                        .skull(friend.getName())
                        .name(friend.getName())
                        .lore("§cClick to remove friend")
                        .build(), ctx -> {
                    playerDataManager.removeFriend(player, friend);
                    player.sendMessage("§cYou have removed " + friend.getName() + " from your friends list");
                    if (friend.isOnline()) {
                        friend.getPlayer().sendMessage("§c" + player.getName() + " has removed you from their friends list");
                    }
                    gui.update(player);
                }));
            });
        });

        border.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE).name(" ").build()));

        gui.addPane(pane);
        gui.addPane(border);
        gui.open(player);
    }
}
