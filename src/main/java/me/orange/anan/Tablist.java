package me.orange.anan;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.tablist.TabColumn;
import io.fairyproject.mc.tablist.TablistAdapter;
import io.fairyproject.mc.tablist.util.Skin;
import io.fairyproject.mc.tablist.util.TabSlot;
import me.orange.anan.clan.Clan;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.player.job.Job;
import me.orange.anan.player.job.JobManager;
import me.orange.anan.player.PlayerDataManager;
import me.orange.anan.world.TimeManager;
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
    private final TimeManager timeManager;
    private final JobManager jobManager;

    public Tablist(ClanManager clanManager, PlayerDataManager playerDataManager, TimeManager timeManager, JobManager jobManager) {
        this.clanManager = clanManager;
        this.playerDataManager = playerDataManager;
        this.timeManager = timeManager;
        this.jobManager = jobManager;
    }

    @Override
    public @Nullable Set<TabSlot> getSlots(MCPlayer player) {
        Set<TabSlot> slots = new HashSet<>();
        slots.add(new TabSlot()
                .column(TabColumn.LEFT)
                .slot(1)
                .text(Component.text("§7§m------------------------------------------------------------------------------------------------------------------------"))
        );
        slots.add(new TabSlot()
                .column(TabColumn.LEFT)
                .slot(20)
                .text(Component.text("§7§m------------------------------------------------------------------------------------------------------------------------"))
        );

        //Server info
        slots.add(new TabSlot()
                .column(TabColumn.LEFT)
                .slot(2)
                .text(Component.text("§bServer Info "))
                .skin(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTYyODE3ODczOTQzNywKICAicHJvZmlsZUlkIiA6ICI5MzI0N2IzMzllMTQ0MDBkYjk5Y2ViM2Y0NzA4ZTBhNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJBemFyb3dfIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2EzMWI3ZDdlNjM0ZjBhZjA1MTFmYzg0NzYxMGE4NjVlYzFjODQwMjM0MTAwMDg5M2FiNjM5NzlmYzhlODVlMzUiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                        "lKjhX6fDX0JxC++G/xo9k4q7psvLx7HAadzdQ9iasfYBw3jCzqVgXaHkhlNyN+iluFHevPqJ0nLobatuATyjexKASOE5wRQmL9WgdJnYEaQnb8+abE2sei2eS6tHGVkzYYbFmv4GmzcpA/CIk2HeSguVhZ4+zxQcnJYVFQqi2qyUo5ZMwLQ9ZiNaVy29LHYcIQp5yGBGFcoZIj0StLhrPOtN8GkBSLaTah2wUMtyAFGa5GJivd0lYh+7MPMZX6MIoZgtmCQC1ZLtiJmyByQv8mAfTAdcNKVpXoK0/F+RjkHIQkQ7hRfIV+g41l7H4SmfW53UMpxv7NXE4gv6Fu3OzSGE6Wmg2BkWbOTj4oIqsnfRPEFozesNwqfgQidP0ngqnRHIG5eX8iI6cXwFNFX7RFTXtIpJ8JhWOLyFAAFcdW46skCIKECm7eH0pEgJl7Q/22hnAz8rnoSh+irxbLQtTeyekUoPSpAPFPtp96Ud401Q7F30gYgpcrZztQ+hxbEnapLFv9l+4fda4USub4+9MlYaWpLWv+a+6EexLYOu163GCdEbkLdQsj2qPk87pRicw3v5pBq7ZnLN3nBGFU5WBsrfOggYfxnrlUDhyK3A7nZcTwc4qAZjS3ptWrk+HydQBNn09Zf8E+kdHArqy8ov0CMcwNFt3r0SOwvnvuxC7V8="))
        );

        slots.add(new TabSlot()
                .column(TabColumn.LEFT)
                .slot(3)
                .text(Component.text("§fWorld: " + player.as(Player.class).getWorld().getName()))
                .skin(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTcwMTM3MzQ1NDk2MSwKICAicHJvZmlsZUlkIiA6ICJkMTViODBlNmQxMzk0OWE1OGMxMWY5YzQzZTQ0ZGZlYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCeUVaYmVyQ2ltZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mMGQ5MGRhODVlNDg4NjRmYTVjNmU5YWY1ZWJmZjgwY2NkNzFkZDViZjE4NTU5ZGI2NGFkYmIwZmQzYTgwMjgxIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
                        "LyazBj5gY/LvBHNjkihAMvtnD9fOuVze0+vAXbzLLb8FYr9aZ75oUF5fmM/rrt1IR2Xg5hjgGnNXwrH3Lg/xJ6YdQ/uhQYYROZn1nWomgTJWeAagJ0NRLkm8aggJ1jM690oPYEmSsijO3HPF9LuEWxdc8CTwnHzje/OBmDeDqiQSKeKVf63aGZaSDfyGWbCdRj3CRGE5btBn5ojT0/zVDuxtvvqN8MuxPZW6cqYzxoUxqzJ9j01fGCcaYQpK/A0R+ciXJ0uhbPtuGRT8Sv/LQZSip7C3pSlvaK9e6mEuE4v5QzVC8V9k5FfGcuGFIlsQtBvw30WXNDfnQzbF5lGdpqdiCDTHlu1/AYh/FQU6OPNv4/xYHH6JigZjyWYOElh96EX1+JDdV80Bu7f99BgFmciokKT5TfI5swTXtYIQfdJN93nAxh85CkJW9xGSOJI2qZTeb+TG1lkntrqXwO599m2UABwQLu+rUNd+yx15SRF6h6G+cw5SBm0dU2bbwvEwjS7tbNjhK7l/vjHHChcJZO9VP4MsEia32UXil8x7wqoetsJ5xNtn7C1Nf/jSxrtkxyh19+8zVXEv2SI4Fxokc/NIGF0Q+3JCzr19PFNMsnoPzNYVqEAXIvwxOt0xKleBTGeHkkTRdsp9yP7BDG4z64gQuqf6+2ualTyyRuhiPtE="))
        );

        slots.add(new TabSlot()
                .column(TabColumn.LEFT)
                .slot(6)
                .text(Component.text("§bTime:"))
                .skin(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTY2MDcwNDE3Mzc2OSwKICAicHJvZmlsZUlkIiA6ICIwNTkyNTIxZGNjZWE0NzRkYjE0M2NmMDg2MDA1Y2FkNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJwdXIyNCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xMTdkYWI5MDhiM2IyYjRhYTdlYWM5MDU0MTYyY2U5N2VkZjJlZjViM2E1MjljOGNkNjRkZmI4ODg0NDhlNDAiCiAgICB9CiAgfQp9",
                        "ShcCfrt/dkIJSZMkZ+vdIMEwups9gidPv9ni9wsfGfpX8L8HviEUgnUbxwhKaaQM/n0Xu1H6NV1ibDVEgxBsUOZ6G9UBvS/V8rn/TeLnULaNbxTpjy6Zv0aH5KsPeLaX3mVcCy5zCT3nuZXCCM6AZgQUfRjltCkvWtp9Tw6rAtHqPCBdP3zOFie6RuhAQfLKfXUQDWf8Aku0N3mxtXjcU9i9/IroNYNz4+z1lz0OpgrB4K6AfXO7TpMW0zlz3we/wEX82L6GtSGnr9JnUUdt9e4XHFrZbMIKzAvk/7lx+LfOEhpNpNhXiA2Lc3C3M+HGwauQ6+hHBz+s3bMcpg732gzKy0sHKz8XuLwm5R8KO0nnR5K5ZeH9RJD4lZDQj28qKMF2GzMnzmgX03OfGQUPVemGwK1qbY9eIiQwRY4+4HUHBbB6ibBQD9eOFMh4J7T5XY1mmKdoQEhofNxmBvxt9pJd4+huMl3KL6uncQMV5KYil5PzQsgswJvjdAgqHfK2wI6kkOHq4qy7r+ci3EvR53qASZ2S//3L6kRRAX+LkLp8f8mzTobAONhDkOENCpKvEW/sl12u8P184Sngfk6Mfw8raB7MmKMbr1pygX2tCK+BvkaICnfQAlRr5mdeKNJEKCpzFSxpYDFvFaNAjiPDtmatMu7Fv+SvmRozrIkE6Ek="))
        );

        Player bukkitPlayer = player.as(Player.class);//頭上
        slots.add(new TabSlot()
                .column(TabColumn.LEFT)
                .slot(7)
                .text(Component.text("§f" + timeManager.getTimeState(bukkitPlayer.getWorld())))
        );

        //Player info
        slots.add(new TabSlot()
                .column(TabColumn.MIDDLE)
                .slot(2)
                .text(Component.text("§bPlayer Info"))
        );
        slots.add(new TabSlot()
                .column(TabColumn.MIDDLE)
                .slot(3)
                .text(Component.text("§fID: " + player.getName()))
                .skin(playerDataManager.getPlayerData(player.getUUID()).getSkin())
        );
        slots.add(new TabSlot()
                .column(TabColumn.MIDDLE)
                .slot(4)
                .text(Component.text("§fPing: §a" + player.getPing() + " §fms"))
                .skin(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTY5MDA2Nzc1MTkyMCwKICAicHJvZmlsZUlkIiA6ICJkOTcwYzEzZTM4YWI0NzlhOTY1OGM1ZDQ1MjZkMTM0YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDcmltcHlMYWNlODUxMjciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZmZTEwZmNhODc5ZjBjNTA2YmNiNzVhMTM0ZWEzZWEwYmQ1OTk4MTVjMTJhMDNmZjAwZWE0NTJlMjM2OWNhMyIKICAgIH0KICB9Cn0=",
                        "esobcLeN6/L4+XkSpXncCT/RNhiftL3n9CmE1ffXMv9QrNdftCQp3anitcZSZNP9P7fHPQCdXvtdvoo1tbTIjMcDWGiLhjaa9C/3jOVG3csxOFgNRVBYqAahMxiB63YQmSvTFvH1En2PbAROXcUTi3h8rG5LU1pmVcYaHR6Y+VwxXdtDO7g/72k8jMRB1qf1R4nn1AiYI/RrY7DHoRlcN/xcwa0jnt03VXLtchWJqEBXnNliv9ZOkSwUZZk4Q+OBhiyshHHFSAhTV3j7thaYhScYAFbOQ0mAd2SJHm9HKc8BWjJPbAmxHd4AUZ3VU6qZvbfJeDoCH+549nR3Qx719JgSoXB73P4/JXpNhCtqv9sX/9Uu+FXJPG0CUpl5Q/w+P6aVevTXBrM7SuX77XRIXLnWR8HWiSdESsNDus9EHcwiG2tfdznSoNXL92hm1NK6QbWlcP/b+QaLkc6nRzLwnCC7kXzyDN98g2bEE5JwnCkfjPhi5cGmEKCezF2uoWq+GuUw5p66uHzweIboAEXeJtu58lK6oB3DlcKM3OB3gYKr/I3PvFbY57wUk5SlZqM4+0FT2zSeMhDVIn34QW2Uj/NeQ7Rmg/1Fo0nz0Zreem4j2YHwv3EclfdT+pZbUZWLkEEzlaaVN5fPZGMKI9CB1sgRAEbqJDBbGMJJyGXtFM4="))
        );
        slots.add(new TabSlot()
                .column(TabColumn.MIDDLE)
                .slot(7)
                .text(Component.text("§bJob:"))
                .skin(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTY5NjY0MTIyNTQ4MywKICAicHJvZmlsZUlkIiA6ICIwYTc1N2ZlZjYzODA0Njk3OGExMDExNjc4MDZhM2U0NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJWYXBvcmtpdHRlbiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85OTkyMjQzOWZkYWYyZTllZWEwMmUzMWEyZjIwNzc3ZmQ2YjdjNWUxZjFmMTYwYmJmNDRmOTIwNTBhNjM0MDZhIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
                        "hS3WKWUQ55mpSr/n/Pwi60jfm+nyK1GDrhl7pG/PF31x4KfZshK6xzWjZOa5nqtAizAfNvVzyO1Q4NAiRiiN9K98xKr5UsoJPX3mvYyD0UIAHi48P9QcBxrblk7P1vJb13VIaEVSDGrzSPKe04eWefjEfX8PtAjWLRLZ7E/J26ZvxI15vUU+wkeimqfFcpfCe5xXCTksIvtSu4li3Vl2EZ7+YfvYWbP08oRnjkpESFtcwIgByD/i6dQbVQqXR27RQygRtdqWLm4qCpqN31uC83YfChMYfgK0fFUkpEB27AYAhYbW3c9n+9lafZ4kARsuRsxGNQCbBJhJSxUh1CAba8s0dhZY5u+lwpoS0y4iTGuT9b/eYb2CuBdBJpPKo0BhOzSr7WkaQHKJcdscIRCWKV8DFjTCtEU3njqvHwHe8MfLkYW+nbQFFy1RgSOU2Txnng8V1euJuMx+3r+6M/Nzv+WGFyry2+yh4/z9xd0GfzjnFhoPDMkH4YMmJQDU+0r52CmDS0KQhB+R0vd+CmtQEBNjpGX7m5Da0o72xA2i8TQllEXgB4PlaAycsqqARytUWyYBc86OBLg8IDZ+uC5Uj0vziQwIakU8Afa97OErG137D7e04GvV/qWfz1+CnGF9QrUehlzxu15M04oUlwYpQdUUnsvsxQWZ3DULbrajXsk="))
        );
        String name = "No Job";
        int level = 0;
        if (jobManager.hasJob(player.as(Player.class))) {
            Job currentJob = jobManager.getPlayerCurrentJob(player.getUUID());
            name = currentJob.getName();
            level = jobManager.getPlayerJobLevel(Bukkit.getPlayer(player.getUUID()), currentJob);
        }
        slots.add(new TabSlot()
                .column(TabColumn.MIDDLE)
                .slot(8)
                .text(Component.text("§fName: §6" + name))
        );
        slots.add(new TabSlot()
                .column(TabColumn.MIDDLE)
                .slot(9)
                .text(Component.text("§fLevel: §a" + level))
        );
        slots.add(new TabSlot()
                .column(TabColumn.MIDDLE)
                .slot(12)
                .text(Component.text("§bStatistics"))
                .skin(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTY5MzY4NjU1NzAwMCwKICAicHJvZmlsZUlkIiA6ICI0M2NmNWJkNjUyMDM0YzU5ODVjMDIwYWI3NDE0OGQxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJrYW1pbDQ0NSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xNDhkMTViMWM1NWM0ZWYzMDM1MDQ0NjQ5OTc0NmQxMDQ0NDBmZGEzNmQ3MDM1NDY3ZWVkOWZmYmU1NTY2Y2ZlIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
                        "TzYSxpy2UQb/uk3ORRL6Yxc7xq0zylMlreWEntXgipUBmPTl/3GDX0TMoEuDpjXbuvhc6jJhxwRpdHGn0c/4E6zPnYhfxYB2y9mZ6DndXxCCYGbnBH3kyfnHTksM0VPgVQVDYCqYb+3gcGyEmJD22hbMHt6MF5sz5354J5Mvd+s58ICKaEkKYowRAmeiMLgpY7Ks/oCev+UK3XvpAClWbsXJA7h7+4osw9ALDz4eQs6A+fRY1GDK516wdtItKBxdTWjFwfISA+kAadUK2oHGcma6+wnO06PG/CYayPOL0qnJTTHbr5etBgfXIcWZr2Z1o4OLcwPITm1E4OGQPJ06COcy7W2LUClIrYw145KjDnkBZESrJaanGYlcV21q0QWBiBOgjObkuDIGTQ9E3IBfJxFkdX6J0rM8YKLdHB3bzSHqpoZNbTvcPI7DXjgQ0J7BVq+YFFvposGJR0lkcpULSl7qIegScwqwAQUNml4dygQMuJ6Tqc24O5HVqggacmE0HmeOwdPl96sYCz1uOqJ1XD5BoLDk3JHVI8gqLiNufzt34UqjB+DT5fyZF9/KdCdNBUWlPdC6XWxdW7r1MWH7oiOOW3pTnxWiPPHpz17s8dKBeDdRrG4N+pwfJb0mjox+cNSzFAJWX/HM1OQXuIgzn59D4wvS5vMVU35ZrFKZskU="))
        );
        slots.add(new TabSlot()
                .column(TabColumn.MIDDLE)
                .slot(13)
                .text(Component.text("§fKills: " + playerDataManager.getPlayerData(player.getUUID()).getKills()))
        );
        slots.add(new TabSlot()
                .column(TabColumn.MIDDLE)
                .slot(14)
                .text(Component.text("§fDeaths: " + playerDataManager.getPlayerData(player.getUUID()).getDeaths()))
        );

        //Clan info
        String clanName = "No Clan";

        slots.add(new TabSlot()
                .column(TabColumn.RIGHT)
                .slot(2)
                .text(Component.text("§bClan Info"))
                .skin(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTYyODE3ODc1NDAyMSwKICAicHJvZmlsZUlkIiA6ICIyM2YxYTU5ZjQ2OWI0M2RkYmRiNTM3YmZlYzEwNDcxZiIsCiAgInByb2ZpbGVOYW1lIiA6ICIyODA3IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRmOWM3M2EzMTNlNjkxZjY3YzdjZmFkMjRlYTlmY2U4OGFkOGZkNGNkYzE4YjZiODViNTc0M2I0Yzg1ZTVhNmQiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                        "XPrRIarTKPwHpF+HhkrAlVzH3hnzCDJlMZXJFKf7jo9YkYJyTc5D1UQSQlZw6k2k5ZdbEXUp1xonPiYMr9auf88uURz2UvdrDtXkKZ2DD5u9t5f0ktWEA06HijfG7iHERum89xA1RLr0zmMwNcrgCHtteebfZJM3B5JkRYRDjgF3i3D6JYuC79JsQVDAro6aPHzVbxTog3FW7UaQ5FC2cSr88rrE692/dHyJfqcj2Kqy3lIFBaWhUa3YY0B4dCUowVsvdDhMH/Y7kt5yLkc+hCDafZ0+/OptQ+/YppcZDrMC+L7JQBDIy5KrICKKMdHPM2CmNUMK8qUscLKqeIANxZ12WR4526leSD+2cxQKQ7vXxbSy4ujWxV3seUJIwk0ln/RbMvfC2CwmsVW9vUQbqM4Ci/+oyL4qqBtEqPqtfC6OKaxJXkB+zqmRlZpmSsPgXKTpJuBY6ixFigRNpTL4DiT7byZTZZUdDvWXhOAr5NAoSBr3thQ9t8j5saF02clBeA2dmatsty56F/bmgqKg4mpi8I/ICkZ38FGMInq7AUXtQMFgDWpCVlJ3O38KDaZcO9NCjsfzCp8NcfcZ2vSkXxgdVDLHdSm7dgBKyIuofmgztgWzLRq+/LdMMjZcsi89ZUdA2826HxxC1LgZvDRt6o8sQHhegryCKJnKinYMXUA=XPrRIarTKPwHpF+HhkrAlVzH3hnzCDJlMZXJFKf7jo9YkYJyTc5D1UQSQlZw6k2k5ZdbEXUp1xonPiYMr9auf88uURz2UvdrDtXkKZ2DD5u9t5f0ktWEA06HijfG7iHERum89xA1RLr0zmMwNcrgCHtteebfZJM3B5JkRYRDjgF3i3D6JYuC79JsQVDAro6aPHzVbxTog3FW7UaQ5FC2cSr88rrE692/dHyJfqcj2Kqy3lIFBaWhUa3YY0B4dCUowVsvdDhMH/Y7kt5yLkc+hCDafZ0+/OptQ+/YppcZDrMC+L7JQBDIy5KrICKKMdHPM2CmNUMK8qUscLKqeIANxZ12WR4526leSD+2cxQKQ7vXxbSy4ujWxV3seUJIwk0ln/RbMvfC2CwmsVW9vUQbqM4Ci/+oyL4qqBtEqPqtfC6OKaxJXkB+zqmRlZpmSsPgXKTpJuBY6ixFigRNpTL4DiT7byZTZZUdDvWXhOAr5NAoSBr3thQ9t8j5saF02clBeA2dmatsty56F/bmgqKg4mpi8I/ICkZ38FGMInq7AUXtQMFgDWpCVlJ3O38KDaZcO9NCjsfzCp8NcfcZ2vSkXxgdVDLHdSm7dgBKyIuofmgztgWzLRq+/LdMMjZcsi89ZUdA2826HxxC1LgZvDRt6o8sQHhegryCKJnKinYMXUA="))
        );

        Clan clan = clanManager.getPlayerClan(player.getUUID());
        if (clanManager.inClan(player.getUUID())) {
            clanName = clan.getDisplayName();

            clan.getOnlinePlayers().forEach(uuid -> {
                Player p = Bukkit.getPlayer(uuid);
                slots.add(new TabSlot()
                        .column(TabColumn.RIGHT)
                        .slot(clanManager.getPlayerClan(uuid).getOnlinePlayers().indexOf(uuid) + 4)
                        .text(Component.text("§2" + p.getName() + ((clanManager.isOwner(p)) ? "§f**" : ""))).skin(playerDataManager.getPlayerData(p.getUniqueId()).getSkin())
                        .ping(MCPlayer.from(p).getPing()));
            });
        }
        slots.add(new TabSlot()
                .column(TabColumn.RIGHT)
                .slot(3)
                .text(Component.text("§6" + clanName))
        );

        //Clan list
        final int[] currentSlot = {3};

        slots.add(new TabSlot()
                .column(TabColumn.FAR_RIGHT)
                .slot(2)
                .text(Component.text("§bClan List"))
        );
        clanManager.getClanMap().forEach((k, v) -> {
            slots.add(new TabSlot()
                    .column(TabColumn.FAR_RIGHT)
                    .slot(currentSlot[0])
                    .text(Component.text(((v == clan) ? "§a" : "§f") + v.getDisplayName() + " §7(" + v.getPlayers().size() + ")"))
            );
            currentSlot[0]++;
        });
        return slots;

    }

    @Override
    public @Nullable Component getHeader(MCPlayer player) {
        return Component.text("\n§b§lProject Rust §r§f┃ " + Bukkit.getOnlinePlayers().size() + " / 134\n");
    }

    @Override
    public @Nullable Component getFooter(MCPlayer player) {
        return Component.text("\n§7Made by PvpForOrange\n");
    }
}
