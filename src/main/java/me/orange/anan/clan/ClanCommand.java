package me.orange.anan.clan;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import me.orange.anan.clan.config.ClanConfig;
import me.orange.anan.events.PlayerJoinClanEvent;
import me.orange.anan.events.PlayerLeftClanEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@InjectableComponent
@Command(value = {"clan", "c"})
public class ClanCommand extends BaseCommand {
    private final ClanManager clanManager;

    public ClanCommand(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @Command("create")
    public void createClan(BukkitCommandContext ctx, @Arg("Clan Name") String clanName) {
        Player player = ctx.getPlayer();
        if (clanManager.inClan(player)) {
            player.sendMessage("§cYou are already in a clan.");
            return;
        }
        if (clanManager.hasClan(clanName)) {
            player.sendMessage("§cThis clan already exists.");
            return;
        }
        if (clanName.length() > 10) {
            player.sendMessage("§cYour clan name can't be longer than 10 letters!");
            return;
        }
        clanManager.createClan(clanName,player);

        player.sendMessage("§aClan " + clanName + " has been successfully created.");
        Bukkit.getPluginManager().callEvent(new PlayerJoinClanEvent(player));
    }

    @Command("invite")
    public void inviteMember(BukkitCommandContext ctx, @Arg("name") Player player) {
        Player sender = ctx.getPlayer();
        if (!clanManager.inClan(sender)) {
            sender.sendMessage("§cYou are not in a clan!");
            return;
        }
        if (clanManager.inClan(player)) {
            sender.sendMessage("§c" + player.getName() + " is already in a clan!");
            return;
        }
        if (clanManager.hasInvitation(sender, player)) {
            sender.sendMessage("§cThis player already has an invitation from your clan.");
            return;
        }

        clanManager.addInvitation(sender, player);
        sender.sendMessage("§eInvitation sent to " + player.getName());
        player.sendMessage("§ePlayer " + sender.getName() + " has sent you an invitation to their clan");

        Component accept = Component.text("§a[Accept]")
                .hoverEvent(HoverEventSource.unbox(Component.text("§7Click to join clan")))
                .clickEvent(ClickEvent.runCommand("/clan accept " + sender.getName()));
        Component deny = Component.text("§c[Deny]")
                .hoverEvent(HoverEventSource.unbox(Component.text("§7Click to deny")))
                .clickEvent(ClickEvent.runCommand("/clan deny " + sender.getName()));
        Component message = Component.text().append(accept).append(Component.text(" or ")).append(deny).build();

        MCPlayer.from(player).sendMessage(message);
    }

    @Command("accept")
    public void acceptInvitation(BukkitCommandContext ctx, @Arg("name") Player invitor) {
        Player player = ctx.getPlayer();
        if (!clanManager.hasInvitation(invitor, player)) {
            player.sendMessage("§cYou didn't receive an invitation from this player's clan.");
            return;
        }

        Clan clan = clanManager.getPlayerClan(invitor);
        invitor.sendMessage(player.getName() + "§a accepted the invitation");
        player.sendMessage("§eYou joined " + clan.getDisplayName());
        clan.addPlayer(player);
        clan.getInvitations().remove(player.getUniqueId());
        clanManager.addPlayerToClan(invitor, player);

        Bukkit.getPluginManager().callEvent(new PlayerJoinClanEvent(player));
    }

    @Command("deny")
    public void denyInvitation(BukkitCommandContext ctx, @Arg("name") Player invitor) {
        Player player = ctx.getPlayer();
        if (!clanManager.hasInvitation(invitor, player)) {
            player.sendMessage("§cYou didn't receive an invitation from this player's clan.");
            return;
        }

        invitor.sendMessage(player.getName() + "§c denied the invitation");
        player.sendMessage("§eYou denied " + invitor.getName() + "'s invite to join " + clanManager.getPlayerClan(invitor).getDisplayName());
        clanManager.getPlayerClan(invitor).getInvitations().remove(player.getUniqueId());
    }

    @Command("leave")
    public void leaveClan(BukkitCommandContext ctx) {
        Player player = ctx.getPlayer();
        if (!clanManager.inClan(player)) {
            player.sendMessage("§cYou are not in a clan.");
            return;
        }
        if (clanManager.isOwner(player)) {
            clanManager.sendOnlineClanPlayer(player, "§eThe clan is disbanded because the owner left!");
            clanManager.clanPlayerEvent(player, new PlayerLeftClanEvent(player));
            clanManager.removePlayerFromClan(player);
            clanManager.getClanMap().remove(clanManager.getPlayerClan(player).getDisplayName());
        } else {
            clanManager.sendClanOwner(player, "§ePlayer " + player.getName() + " has left the clan.");
            clanManager.getPlayerClan(player).removePlayer(player);
            player.sendMessage("§eYou left the clan.");
            Bukkit.getPluginManager().callEvent(new PlayerLeftClanEvent(player));
        }
    }

    @Command("list")
    public void listMember(BukkitCommandContext ctx) {
        Player player = ctx.getPlayer();
        if (!clanManager.inClan(player)) {
            player.sendMessage("§cYou are not in a clan.");
            return;
        }

        Clan clan = clanManager.getPlayerClan(player);
        player.sendMessage("§eClan: §6" + clanManager.getClanName(player));
        player.sendMessage("§eOwner: §b" + clanManager.getOwnerName(player));
        player.sendMessage("§eOnline Players: ");
        clan.getOnlineBukkitPlayers().forEach(member -> player.sendMessage("§a" + member.getName()));
        player.sendMessage("§eOffline Players: ");
        clan.getOfflineBukkitPlayers().forEach(member -> player.sendMessage("§7" + member.getName() + ", "));
    }

    @Command("disband")
    public void disbandClan(BukkitCommandContext ctx) {
        Player player = ctx.getPlayer();
        if (!clanManager.inClan(player)) {
            player.sendMessage("§cYou are not in a clan!");
            return;
        }
        if (!clanManager.isOwner(player)) {
            player.sendMessage("§cYou are not the owner!");
            return;
        }

        clanManager.sendOnlineClanPlayer(player, "§eThis clan has been disbanded by the clan owner");
        clanManager.clanPlayerEvent(player, new PlayerLeftClanEvent(player));
        clanManager.getClanMap().remove(clanManager.getPlayerClan(player).getDisplayName());
    }

    @Command("kick")
    public void kickMember(BukkitCommandContext ctx, @Arg("Name") Player player) {
        Player sender = ctx.getPlayer();
        if (!clanManager.isOwner(sender)) {
            sender.sendMessage("§cYou don't have the permission to do this.");
            return;
        }
        if (!clanManager.sameClan(sender, player)) {
            sender.sendMessage("§cThis player is not in the same clan as you!");
            return;
        }
        if (clanManager.isOwner(player)) {
            sender.sendMessage("§cYou cannot kick yourself!");
            return;
        }

        clanManager.removePlayerFromClan(player);
        player.sendMessage("§eYou have been removed from the clan by the owner.");
        sender.sendMessage(player.getName() + "§e has been removed from the clan.");
    }

    @Command("rename")
    public void renameClan(BukkitCommandContext ctx, @Arg("New Name") String name) {
        Player player = ctx.getPlayer();
        if (!clanManager.isOwner(player)) {
            player.sendMessage("§cYou don't have the permission to do this.");
            return;
        }
        if (name.length() > 10) {
            player.sendMessage("§cYour clan name can't be longer than 10 letters!");
            return;
        }

        clanManager.getPlayerClan(player).setDisplayName(name);
        player.sendMessage("§aYou have changed your clan name to " + name);
    }

    @Command("transfer")
    public void transferOwner(BukkitCommandContext ctx, @Arg("New Owner") Player player) {
        if (!clanManager.sameClan(ctx.getPlayer(), player)) {
            if (clanManager.isOwner(ctx.getPlayer())) {
                clanManager.getPlayerClan(ctx.getPlayer()).setOwner(player.getUniqueId());
                player.sendMessage("§aYou are the clan owner now");
                ctx.getPlayer().sendMessage(player.getName() + "§e has become the owner.");
            } else {
                ctx.getPlayer().sendMessage("§cYou don't have the permission to do this.");
            }
        } else {
            ctx.getPlayer().sendMessage("§cYou are not in the same clan with this player!");
        }
    }

    @Command("owner")
    public void showOwner(BukkitCommandContext ctx){
        ctx.getPlayer().sendMessage(clanManager.getOwnerName(ctx.getPlayer()));
    }
}



