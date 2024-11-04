package me.orange.anan.player.settings;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@InjectableComponent
@Command("hint")
public class HintCommand extends BaseCommand {
    @Command("#")
    public void hint(BukkitCommandContext ctx) {
        Player player = ctx.getPlayer();
        Component bookTitle = Component.text("伺服器指南")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD);
        Component bookAuthor = Component.text("伺服器管理團隊")
                .color(NamedTextColor.DARK_GREEN);

        List<Component> bookPages = Arrays.asList(
                // Introduction Page
                Component.text("§6歡迎來到曠野伺服器！\n\n")
                        .append(Component.text("§2這是一個獨特的生存世界，擁有許多挑戰和驚喜。\n"))
                        .append(Component.text("§3我們希望您能在此享受每一刻，和其他玩家一同探索、合作，共度冒險！")),

                // Swimming Tip
                Component.text("§6游泳小技巧\n\n")
                        .append(Component.text("§2在水中時，長按"))
                        .append(Component.text("§6Shift ")
                                .decorate(TextDecoration.BOLD))
                        .append(Component.text("§2可以更快速地游泳！")),

                // Doors and Locks
                Component.text("§6門與鎖\n\n")
                        .append(Component.text("§2可以合成鎖來保護門，防止他人互動。\n"))
                        .append(Component.text("§3適用於木門、陷阱門與柵欄門。")),

                // Bed and Respawn Points
                Component.text("§6床與重生點\n\n")
                        .append(Component.text("§2放置床可設置多個重生點，右鍵點擊可命名。")),

                // Team Core Placement
                Component.text("§6隊伍核心\n\n")
                        .append(Component.text("§c您已放置隊伍核心！\n"))
                        .append(Component.text("§3若建築方塊未連接到核心，將會在一段時間後消失。")),

                // Night Warning
                Component.text("§6夜晚來臨\n\n")
                        .append(Component.text("§c夜晚已到來！您將無法看到10格外的敵人。")),

                // TNT Usage
                Component.text("§6TNT的使用\n\n")
                        .append(Component.text("§2TNT無須點燃即可爆炸！\n"))
                        .append(Component.text("§3對建築物造成300點傷害。")),

                // Task Goals
                Component.text("§6任務目標\n\n")
                        .append(Component.text("§2可在個人資訊頁面的任務列表查看目標。")),

                // Task Rewards
                Component.text("§6獎勵領取\n\n")
                        .append(Component.text("§2完成任務後，前往任務NPC領取獎勵！")),

                // Player Down State
                Component.text("§6倒地狀態\n\n")
                        .append(Component.text("§c您已倒地！隊友可右鍵點擊您進行救援。")),

                // Safe Zone
                Component.text("§6安全區域\n\n")
                        .append(Component.text("§2您已進入安全區域！\n"))
                        .append(Component.text("§3在此可與NPC交易並接受任務。"))
        );

        Book myBook = Book.book(bookTitle, bookAuthor, bookPages);
        Objects.requireNonNull(MCPlayer.from(player)).openBook(myBook);
    }
}
