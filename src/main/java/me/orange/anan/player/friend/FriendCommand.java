package me.orange.anan.player.friend;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import me.orange.anan.player.PlayerDataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@InjectableComponent
@Command(value = {"friend", "f"})
public class FriendCommand extends BaseCommand {
    private final PlayerDataManager playerDataManager;

    public FriendCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @Command(value = {"add"})
    public void addFriend(BukkitCommandContext ctx, @Arg("name") Player player) {
        Player invitor = ctx.getPlayer();
        if (invitor.equals(player)) {
            invitor.sendMessage("§cYou can't add yourself as a friend");
            return;
        }
        if (playerDataManager.isFriend(invitor, player)) {
            invitor.sendMessage("§cYou are already friends with " + player.getName());
            return;
        }
        if (playerDataManager.hasInvitation(invitor, player)) {
            invitor.sendMessage("§eYou already have a pending invitation to " + player.getName());
            return;
        }
        playerDataManager.addFriendRequest(invitor, player);
        invitor.sendMessage("§eYou have sent a friend request to " + player.getName());
        player.sendMessage("§eYou have received a friend request from " + invitor.getName());

        Component accept = Component.text("§a[Accept]")
                .hoverEvent(HoverEventSource.unbox(Component.text("§7Click to accept")))
                .clickEvent(ClickEvent.runCommand("/friend accept " + invitor.getName()));
        Component deny = Component.text("§c[Deny]")
                .hoverEvent(HoverEventSource.unbox(Component.text("§7Click to deny")))
                .clickEvent(ClickEvent.runCommand("/friend deny " + invitor.getName()));
        Component message = Component.text().append(accept).append(Component.text(" or ")).append(deny).build();

        MCPlayer.from(player).sendMessage(message);
    }

    @Command(value = {"accept"})
    public void acceptInvitation(BukkitCommandContext ctx, @Arg("name") Player invitor) {
        Player player = ctx.getPlayer();
        if (!playerDataManager.hasInvitation(invitor, player)) {
            invitor.sendMessage("§cYou don't have a pending invitation from " + invitor.getName());
            return;
        }
        playerDataManager.acceptFriendRequest(invitor, player);
        invitor.sendMessage("§aYou are now friends with " + player.getName());
        player.sendMessage("§aYou are now friends with " + invitor.getName());
    }

    @Command(value = {"deny"})
    public void denyInvitation(BukkitCommandContext ctx, @Arg("name") Player invitor) {
        Player player = ctx.getPlayer();
        if (!playerDataManager.hasInvitation(invitor, player)) {
            invitor.sendMessage("§cYou don't have a pending invitation from " + player.getName());
            return;
        }
        playerDataManager.denyFriendRequest(invitor, player);
        invitor.sendMessage("§cYou have denied the friend request from " + player.getName());
        player.sendMessage("§c" + invitor.getName() + " has denied your friend request");
    }

    @Command(value = {"remove"})
    public void removeFriend(BukkitCommandContext ctx, @Arg("name") Player player) {
        Player invitor = ctx.getPlayer();
        if (!playerDataManager.isFriend(invitor, player)) {
            invitor.sendMessage("§cYou are not friends with " + player.getName());
            return;
        }
        playerDataManager.removeFriend(invitor, player);
        invitor.sendMessage("§cYou have removed " + player.getName() + " from your friends list");
        player.sendMessage("§c" + invitor.getName() + " has removed you from their friends list");
    }

    @Command(value = {"list"})
    public void listFriends(BukkitCommandContext ctx) {
        Player player = ctx.getPlayer();
        StringBuilder message = new StringBuilder("§eFriends: \n");
        playerDataManager.getFriends(player).forEach(uuid -> {
            OfflinePlayer friend = Bukkit.getOfflinePlayer(uuid);
            message.append((friend.isOnline())?"§a":"§7").append(friend.getName()).append("§e, ");
        });
        player.sendMessage(message.toString());
    }
}
