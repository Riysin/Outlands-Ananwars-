package me.orange.anan.player.settings;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import me.orange.anan.events.*;
import me.orange.anan.player.PlayerDataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
@RegisterAsListener
public class HintEventListener implements Listener {
    private final PlayerDataManager playerDataManager;

    public HintEventListener(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!playerDataManager.getPlayerData(player).isHint()) {
            return;
        }
        MCPlayer mcPlayer = MCPlayer.from(player);
        assert mcPlayer != null;
        mcPlayer.sendMessage(Component.text("§e歡迎加入伺服器")
                .hoverEvent(Component.text("§e點擊這裡查看更多信息"))
                .clickEvent(ClickEvent.runCommand("/hint")));
    }

    private void sendHint(Player player, String hint) {
        if (playerDataManager.getPlayerData(player).isHint())
            player.sendMessage("§e[提示] §7" + hint);
    }

    @EventHandler
    public void onPlayerInWater(PlayerMoveEvent event) {
        if (!isWater(event.getFrom().getBlock()) && isWater(event.getTo().getBlock())) {
            sendHint(event.getPlayer(), "在水中長按shift游泳");
        }
    }

    private boolean isWater(Block block) {
        return block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Material type = event.getBlock().getType();
        Player player = event.getPlayer();

        //lockable
        if (type == Material.WOODEN_DOOR
                || type == Material.IRON_DOOR
                || type == Material.IRON_DOOR_BLOCK
                || type == Material.TRAP_DOOR
                || type == Material.FENCE_GATE) {
            sendHint(player, "您可以在合成並使用鎖來將門上鎖來防止他人與此方塊互動");
        }

        // bed
        if (type == Material.BED_BLOCK) {
            sendHint(player, "您可以使用床來設置多個重生點，並且可以按右鍵命名");
        }
    }

    @EventHandler
    public void onTeamCorePlace(PlayerPlaceTeamCoreEvent event) {
        sendHint(event.getPlayer(), "您放置了隊伍核心，如果建築方塊無法連接到核心，該建築方塊會在一段時間後消失");
    }

    @EventHandler
    public void dayChangeEvent(DayToNightEvent event) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            sendHint(player, "夜晚已到來，玩家將無法看到10格外的敵人");
        });
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());

        if (item != null && item.getType() == Material.TNT) {
            sendHint(player, "TNT無須點燃即可引爆，可對建築造成300點傷害");
        }
    }

    @EventHandler
    public void onTaskAccepted(TaskAcceptEvent event) {
        sendHint(event.getPlayer(), "您可以個人資訊頁面中的任務列表查看任務目標");
    }

    @EventHandler
    public void onTaskComplete(TaskCompleteEvent event) {
        sendHint(event.getPlayer(), "請前往任務NPC領取獎勵");
    }

    @EventHandler
    public void onPlayerDown(PlayerDownEvent event) {
        sendHint(event.getPlayer(), "你倒地了，同隊的隊友可以對你按右鍵救援");
    }

    @EventHandler
    public void onEnterSafeZone(PlayerEnterSafeZoneEvent event) {
        sendHint(event.getPlayer(), "您已進入安全區域，可在此與NPC交易及做任務");
    }
}
