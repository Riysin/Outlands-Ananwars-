package me.orange.anan.settings;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@InjectableComponent
@Command("hint")
public class HintCommand extends BaseCommand {
    @Command("#")
    public void hint(BukkitCommandContext ctx) {
        Player player = ctx.getPlayer();
        Component bookTitle = Component.text("Outlands");
        Component bookAuthor = Component.text("an Anan player");

        Collection<Component> bookPages = Arrays.asList(
            Component.text("Cats are small carnivorous mammals."),
            Component.text("They are often called house cats when kept as indoor pets"));

        Book myBook = Book.book(bookTitle, bookAuthor, bookPages);
        Objects.requireNonNull(MCPlayer.from(player)).openBook(myBook);
    }
}
