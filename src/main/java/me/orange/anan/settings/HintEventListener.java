package me.orange.anan.settings;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import me.orange.anan.events.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

@InjectableComponent
@RegisterAsListener
public class HintEventListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MCPlayer mcPlayer =MCPlayer.from(player);
        assert mcPlayer != null;
        mcPlayer.sendMessage(Component.text("§e歡迎加入伺服器")
                .hoverEvent(Component.text("§e點擊這裡查看更多信息"))
                .clickEvent(ClickEvent.runCommand("/hint")));
    }

    private void provideHint(Player player, String hint) {
        player.sendMessage("§e" + hint);
    }

    @EventHandler
    public void onPlayerInWater(PlayerMoveEvent event) {
        if (event.getPlayer().getLocation().getBlock().getType() == Material.WATER || event.getPlayer().getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
            event.getPlayer().sendMessage("§e你正在水中");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Material type = event.getBlock().getType();

        //lockable
        if (type == Material.WOODEN_DOOR
                || type == Material.IRON_DOOR
                || type == Material.IRON_DOOR_BLOCK
                || type == Material.TRAP_DOOR
                || type == Material.FENCE_GATE) {
            event.getPlayer().sendMessage("§e你放置了鑽石方塊");
        }

        // bed
        if (type == Material.BED_BLOCK) {
            event.getPlayer().sendMessage("§e你放置了床");
        }
    }

    @EventHandler
    public void onTeamCorePlace(PlayerPlaceTeamCoreEvent event) {
        event.getPlayer().sendMessage("§e你放置了隊伍核心");
    }

    @EventHandler
    public void dayChangeEvent(DayToNightEvent event) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("§e夜晚已到來"));
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        if (event.getPlayer().getItemInHand().getType().equals(Material.TNT)){
            event.getPlayer().sendMessage("§e你拿著TNT");
        }
    }

    @EventHandler
    public void onTaskAccepted(TaskAcceptEvent event) {
        event.getPlayer().sendMessage("§e你接受了任務");
    }

    @EventHandler
    public void onTaskComplete(TaskCompleteEvent event) {
        event.getPlayer().sendMessage("§e你完成了任務");
    }

    @EventHandler
    public void onPlayerDown(PlayerDownEvent event) {
        event.getPlayer().sendMessage("§e你掉落了");
    }
}
