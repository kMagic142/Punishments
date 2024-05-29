package ro.kmagic.punishments.listeners;

import ro.kmagic.punishments.Punishments;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayer;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayerRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        ResultSet set = Punishments.getPlugin().getDatabase().query("SELECT * FROM " + Punishments.tablePrefix + "players WHERE uuid='" + player.getUniqueId() + "'");

        MinecraftPlayer mp = Objects.requireNonNull(MinecraftPlayerRepository.getPlayerByUuid(player.getUniqueId()));

        try {
            if (set.next()) if (mp.getBanned()) e.setQuitMessage(null);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}
