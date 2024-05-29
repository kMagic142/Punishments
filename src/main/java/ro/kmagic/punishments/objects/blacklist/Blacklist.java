package ro.kmagic.punishments.objects.blacklist;

import lombok.Getter;
import ro.kmagic.punishments.Punishments;

@Getter
public class Blacklist {

    @Getter
    private String uuid;

    @Getter
    private String username;

    @Getter
    private String reason;

    @Getter
    private String admin;

    @Getter
    private int id;

    public Blacklist(String uuid, String username, String reason, String admin, int id) {
        this.uuid = uuid;
        this.username = username;
        this.reason = reason;
        this.admin = admin;
        this.id = id;
    }

    public void updateSql() {
        String sql = "UPDATE `" + Punishments.getPlugin().getDatabase().getDatabase() + "`.`blacklists` SET" +
                "uuid='" + uuid + "'," +
                "username='" + username + "'," +
                "reason='" + reason + "'," +
                "admin='" + admin + "'," +
                "id='" + id + "' WHERE uuid='" + uuid + "'";
        Punishments.getPlugin().getDatabase().execute(sql);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
        updateSql();
    }

    public void setUsername(String username) {
        this.username = username;
        updateSql();
    }

    public void setReason(String reason) {
        this.reason = reason;
        updateSql();
    }

    public void setAdmin(String admin) {
        this.admin = admin;
        updateSql();
    }

    public void setId(int id) {
        this.id = id;
        updateSql();
    }

    @Override
    public String toString() {
        return "Ban{" +
                "uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                ", reason='" + reason + '\'' +
                ", adm";
    }
}