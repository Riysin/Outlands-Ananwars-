package me.orange.anan;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.tablist.TabColumn;
import io.fairyproject.mc.tablist.TablistAdapter;
import io.fairyproject.mc.tablist.util.Skin;
import io.fairyproject.mc.tablist.util.TabSlot;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.player.PlayerDataManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@InjectableComponent
public class Tablist implements TablistAdapter {
    private final ClanManager clanManager;
    private final PlayerDataManager playerDataManager;

    public Tablist(ClanManager clanManager, PlayerDataManager playerDataManager) {
        this.clanManager = clanManager;
        this.playerDataManager = playerDataManager;
    }

    @Override
    public @Nullable Set<TabSlot> getSlots(MCPlayer player) {
        Set<TabSlot> slots = new HashSet<>();
        slots.add(new TabSlot()
                .column(TabColumn.LEFT)
                .slot(1)
                .text(Component.text("§7§m----------------------------------------------------------------------------------------------------------------------------"))
        );
        slots.add(new TabSlot()
                .column(TabColumn.LEFT)
                .slot(20)
                .text(Component.text("§7§m----------------------------------------------------------------------------------------------------------------------------"))
        );

        //Player info
        slots.add(new TabSlot()
                .column(TabColumn.LEFT)
                .slot(2)
                .text(Component.text("§b玩家資料: "))
        );
        slots.add(new TabSlot()
                .column(TabColumn.LEFT)
                .slot(3)
                .text(Component.text("§6ID: §f" + player.getName()))
                .skin(playerDataManager.getPlayerData(player.getUUID()).getSkin())
        );
        slots.add(new TabSlot()
                .column(TabColumn.LEFT)
                .slot(4)
                .text(Component.text("§6Ping: §f" + player.getPing() + "ms"))
                .skin(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTY5MDA2Nzc1MTkyMCwKICAicHJvZmlsZUlkIiA6ICJkOTcwYzEzZTM4YWI0NzlhOTY1OGM1ZDQ1MjZkMTM0YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDcmltcHlMYWNlODUxMjciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZmZTEwZmNhODc5ZjBjNTA2YmNiNzVhMTM0ZWEzZWEwYmQ1OTk4MTVjMTJhMDNmZjAwZWE0NTJlMjM2OWNhMyIKICAgIH0KICB9Cn0=",
                        "esobcLeN6/L4+XkSpXncCT/RNhiftL3n9CmE1ffXMv9QrNdftCQp3anitcZSZNP9P7fHPQCdXvtdvoo1tbTIjMcDWGiLhjaa9C/3jOVG3csxOFgNRVBYqAahMxiB63YQmSvTFvH1En2PbAROXcUTi3h8rG5LU1pmVcYaHR6Y+VwxXdtDO7g/72k8jMRB1qf1R4nn1AiYI/RrY7DHoRlcN/xcwa0jnt03VXLtchWJqEBXnNliv9ZOkSwUZZk4Q+OBhiyshHHFSAhTV3j7thaYhScYAFbOQ0mAd2SJHm9HKc8BWjJPbAmxHd4AUZ3VU6qZvbfJeDoCH+549nR3Qx719JgSoXB73P4/JXpNhCtqv9sX/9Uu+FXJPG0CUpl5Q/w+P6aVevTXBrM7SuX77XRIXLnWR8HWiSdESsNDus9EHcwiG2tfdznSoNXL92hm1NK6QbWlcP/b+QaLkc6nRzLwnCC7kXzyDN98g2bEE5JwnCkfjPhi5cGmEKCezF2uoWq+GuUw5p66uHzweIboAEXeJtu58lK6oB3DlcKM3OB3gYKr/I3PvFbY57wUk5SlZqM4+0FT2zSeMhDVIn34QW2Uj/NeQ7Rmg/1Fo0nz0Zreem4j2YHwv3EclfdT+pZbUZWLkEEzlaaVN5fPZGMKI9CB1sgRAEbqJDBbGMJJyGXtFM4="))
        );

        //Clan info
        String clanName = "無";

        slots.add(new TabSlot()
                .column(TabColumn.MIDDLE)
                .slot(2)
                .text(Component.text("§b隊伍狀態: "))
                .skin(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTYyODE3ODc1NDAyMSwKICAicHJvZmlsZUlkIiA6ICIyM2YxYTU5ZjQ2OWI0M2RkYmRiNTM3YmZlYzEwNDcxZiIsCiAgInByb2ZpbGVOYW1lIiA6ICIyODA3IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRmOWM3M2EzMTNlNjkxZjY3YzdjZmFkMjRlYTlmY2U4OGFkOGZkNGNkYzE4YjZiODViNTc0M2I0Yzg1ZTVhNmQiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                        "XPrRIarTKPwHpF+HhkrAlVzH3hnzCDJlMZXJFKf7jo9YkYJyTc5D1UQSQlZw6k2k5ZdbEXUp1xonPiYMr9auf88uURz2UvdrDtXkKZ2DD5u9t5f0ktWEA06HijfG7iHERum89xA1RLr0zmMwNcrgCHtteebfZJM3B5JkRYRDjgF3i3D6JYuC79JsQVDAro6aPHzVbxTog3FW7UaQ5FC2cSr88rrE692/dHyJfqcj2Kqy3lIFBaWhUa3YY0B4dCUowVsvdDhMH/Y7kt5yLkc+hCDafZ0+/OptQ+/YppcZDrMC+L7JQBDIy5KrICKKMdHPM2CmNUMK8qUscLKqeIANxZ12WR4526leSD+2cxQKQ7vXxbSy4ujWxV3seUJIwk0ln/RbMvfC2CwmsVW9vUQbqM4Ci/+oyL4qqBtEqPqtfC6OKaxJXkB+zqmRlZpmSsPgXKTpJuBY6ixFigRNpTL4DiT7byZTZZUdDvWXhOAr5NAoSBr3thQ9t8j5saF02clBeA2dmatsty56F/bmgqKg4mpi8I/ICkZ38FGMInq7AUXtQMFgDWpCVlJ3O38KDaZcO9NCjsfzCp8NcfcZ2vSkXxgdVDLHdSm7dgBKyIuofmgztgWzLRq+/LdMMjZcsi89ZUdA2826HxxC1LgZvDRt6o8sQHhegryCKJnKinYMXUA=XPrRIarTKPwHpF+HhkrAlVzH3hnzCDJlMZXJFKf7jo9YkYJyTc5D1UQSQlZw6k2k5ZdbEXUp1xonPiYMr9auf88uURz2UvdrDtXkKZ2DD5u9t5f0ktWEA06HijfG7iHERum89xA1RLr0zmMwNcrgCHtteebfZJM3B5JkRYRDjgF3i3D6JYuC79JsQVDAro6aPHzVbxTog3FW7UaQ5FC2cSr88rrE692/dHyJfqcj2Kqy3lIFBaWhUa3YY0B4dCUowVsvdDhMH/Y7kt5yLkc+hCDafZ0+/OptQ+/YppcZDrMC+L7JQBDIy5KrICKKMdHPM2CmNUMK8qUscLKqeIANxZ12WR4526leSD+2cxQKQ7vXxbSy4ujWxV3seUJIwk0ln/RbMvfC2CwmsVW9vUQbqM4Ci/+oyL4qqBtEqPqtfC6OKaxJXkB+zqmRlZpmSsPgXKTpJuBY6ixFigRNpTL4DiT7byZTZZUdDvWXhOAr5NAoSBr3thQ9t8j5saF02clBeA2dmatsty56F/bmgqKg4mpi8I/ICkZ38FGMInq7AUXtQMFgDWpCVlJ3O38KDaZcO9NCjsfzCp8NcfcZ2vSkXxgdVDLHdSm7dgBKyIuofmgztgWzLRq+/LdMMjZcsi89ZUdA2826HxxC1LgZvDRt6o8sQHhegryCKJnKinYMXUA="))
        );

        if (clanManager.inClan(player.getUUID())) {
            clanName = clanManager.getPlayerClan(player.getUUID()).getDisplayName();

            clanManager.getPlayerClan(player.getUUID()).getOnlinePlayers().forEach(uuid -> {
                Player p = Bukkit.getPlayer(uuid);
                slots.add(new TabSlot()
                        .column(TabColumn.MIDDLE)
                        .slot(clanManager.getPlayerClan(uuid).getOnlinePlayers().indexOf(uuid) + 4)
                        .text(Component.text("§2"+ p.getName())).skin(playerDataManager.getPlayerData(p.getUniqueId()).getSkin())
                        .ping(MCPlayer.from(p).getPing()));
            });
        }
        slots.add(new TabSlot()
                .column(TabColumn.MIDDLE)
                .slot(3)
                .text(Component.text("§6" + clanName))
        );
        //Server info
        slots.add(new TabSlot()
                .column(TabColumn.RIGHT)
                .slot(2)
                .text(Component.text("§b上線人數: "))
                .skin(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTYyODE3ODczOTQzNywKICAicHJvZmlsZUlkIiA6ICI5MzI0N2IzMzllMTQ0MDBkYjk5Y2ViM2Y0NzA4ZTBhNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJBemFyb3dfIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2EzMWI3ZDdlNjM0ZjBhZjA1MTFmYzg0NzYxMGE4NjVlYzFjODQwMjM0MTAwMDg5M2FiNjM5NzlmYzhlODVlMzUiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                        "lKjhX6fDX0JxC++G/xo9k4q7psvLx7HAadzdQ9iasfYBw3jCzqVgXaHkhlNyN+iluFHevPqJ0nLobatuATyjexKASOE5wRQmL9WgdJnYEaQnb8+abE2sei2eS6tHGVkzYYbFmv4GmzcpA/CIk2HeSguVhZ4+zxQcnJYVFQqi2qyUo5ZMwLQ9ZiNaVy29LHYcIQp5yGBGFcoZIj0StLhrPOtN8GkBSLaTah2wUMtyAFGa5GJivd0lYh+7MPMZX6MIoZgtmCQC1ZLtiJmyByQv8mAfTAdcNKVpXoK0/F+RjkHIQkQ7hRfIV+g41l7H4SmfW53UMpxv7NXE4gv6Fu3OzSGE6Wmg2BkWbOTj4oIqsnfRPEFozesNwqfgQidP0ngqnRHIG5eX8iI6cXwFNFX7RFTXtIpJ8JhWOLyFAAFcdW46skCIKECm7eH0pEgJl7Q/22hnAz8rnoSh+irxbLQtTeyekUoPSpAPFPtp96Ud401Q7F30gYgpcrZztQ+hxbEnapLFv9l+4fda4USub4+9MlYaWpLWv+a+6EexLYOu163GCdEbkLdQsj2qPk87pRicw3v5pBq7ZnLN3nBGFU5WBsrfOggYfxnrlUDhyK3A7nZcTwc4qAZjS3ptWrk+HydQBNn09Zf8E+kdHArqy8ov0CMcwNFt3r0SOwvnvuxC7V8="))
        );

        slots.add(new TabSlot()
                .column(TabColumn.RIGHT)
                .slot(3)
                .text(Component.text(Bukkit.getOnlinePlayers().size()+("/134")))
        );

        //Clan list
        final int[] currentSlot = {3};

        slots.add(new TabSlot()
                .column(TabColumn.FAR_RIGHT)
                .slot(2)
                .text(Component.text("§b隊伍清單: "))
        );

        clanManager.getClanMap().forEach((k, v)->{
            slots.add(new TabSlot()
                    .column(TabColumn.FAR_RIGHT)
                    .slot(currentSlot[0])
                    .text(Component.text(v.getDisplayName()))
            );
            currentSlot[0]++;
        });
        return slots;

    }

    @Override
    public @Nullable Component getHeader(MCPlayer player) {
        return Component.text("§b§lTesting");
    }

    @Override
    public @Nullable Component getFooter(MCPlayer player) {
        return Component.text("§7Made by PvpForOrange");
    }
}
