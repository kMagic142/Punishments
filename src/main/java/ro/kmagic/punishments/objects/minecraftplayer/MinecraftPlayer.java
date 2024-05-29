package ro.kmagic.punishments.objects.minecraftplayer;

import ro.kmagic.punishments.Punishments;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
public class MinecraftPlayer {

    private String uuid;
    private Player player;
    private String username;

    private Boolean banned;
    private Timestamp banexpirydate;

    private Boolean muted;
    private Timestamp muteexpirydate;

    private Boolean blacklisted;

    private final Integer warns;
    private Integer kicks;

    public MinecraftPlayer(String uuid, String username, Boolean banned, Timestamp banexpirydate, Boolean muted, Timestamp muteexpirydate, Integer warns, Integer kicks, Boolean blacklisted) {
        this.uuid = Objects.requireNonNullElse(uuid, "");
        this.player = Bukkit.getPlayer(uuid);
        this.username = Objects.requireNonNullElse(username, "");
        this.banned = Objects.requireNonNullElse(banned, false);
        this.blacklisted = Objects.requireNonNullElse(blacklisted, false);
        this.banexpirydate = Objects.requireNonNullElse(banexpirydate, new Timestamp(System.currentTimeMillis()));
        this.muted = Objects.requireNonNullElse(muted, false);
        this.muteexpirydate = Objects.requireNonNullElse(muteexpirydate, new Timestamp(System.currentTimeMillis()));
        this.warns = Objects.requireNonNullElse(warns, 0);
        this.kicks = Objects.requireNonNullElse(kicks, 0);
    }

    public void updateSql() {
        String sql = "DELETE from " + Punishments.getPlugin().getDatabase().getTablePrefix() + "players where uuid='" + uuid + "'";
        Punishments.getPlugin().getDatabase().execute(sql);

        Punishments.getPlugin().getDatabase().insertPlayer(uuid, username, banned, banexpirydate, muted, muteexpirydate, warns, kicks, blacklisted);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
        updateSql();
    }

    public void setPlayer(Player player) {
        this.player = player;
        updateSql();
    }

    public void setUsername(String username) {
        this.username = username;
        updateSql();
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
        updateSql();
    }

    public void setBanexpirydate(Timestamp banexpirydate) {
        this.banexpirydate = banexpirydate;
        updateSql();
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
        updateSql();
    }

    public void setMuteexpirydate(Timestamp muteexpirydate) {
        this.muteexpirydate = muteexpirydate;
        updateSql();
    }

    public void setBlacklisted(Boolean blacklisted) {
        this.blacklisted = blacklisted;
        updateSql();
    }

    public void setKicks(Integer kicks) {
        this.kicks = kicks;
        updateSql();
    }

    @Override
    public String toString() {
        return "MinecraftPlayer{" +
                "uuid='" + uuid + '\'' +
                ", player=" + player +
                ", username='" + username + '\'' +
                ", banned=" + banned +
                ", banexpirydate=" + banexpirydate +
                ", muted=" + muted +
                ", muteexpirydate=" + muteexpirydate +
                ", warns=" + warns +
                ", kicks=" + kicks +
                ", blacklisted=" + blacklisted +
                '}';
    }

}
