package fr.trankilium.trankiliumutilities.events;

import fr.trankilium.trankiliumutilities.globalUtils.SQLiteCore;
import fr.trankilium.trankiliumutilities.utilities.JoinAndLeaveMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.ResultSet;

import static fr.trankilium.trankiliumutilities.Main.dbPath;

public class PlayerJoinEventHandler implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        JoinAndLeaveMessages.joinMessage(event);

        Player player = event.getPlayer();
        SQLiteCore db = new SQLiteCore();
        db.ConnectDB(dbPath);
        ResultSet resultSet = db.Query(String.format("SELECT COUNT(1) FROM rubis WHERE player = '%s'", player.getName().toLowerCase()));
        int count = 0;
        try {
            if (resultSet != null && resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (Exception ignored) {}
        boolean containsPlayer = count > 0;

        if (!containsPlayer) {
            String highestRank = "default";
            if (player.hasPermission("group.divin")) {
                highestRank = "divin";
                if (player.hasPermission("group.celeste")) {
                    highestRank = "celeste";
                    if (player.hasPermission("group.transcendant")) {
                        highestRank = "transcendant";
                    }
                }
            }

            int claimsAmount = 0;
            switch (highestRank) {
                case "default" -> claimsAmount = 4;
                case "divin" -> claimsAmount = 9;
                case "celeste" -> claimsAmount = 16;
                case "transcendant" -> claimsAmount = 25;
            }

            db.Exec(String.format("INSERT INTO rubis (player, claims_amount) VALUES ('%s', %d)", player.getName().toLowerCase(), claimsAmount));
        }
        db.DisconnectDB();
    }

}
