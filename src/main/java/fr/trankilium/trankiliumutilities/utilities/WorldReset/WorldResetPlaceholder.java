package fr.trankilium.trankiliumutilities.utilities.WorldReset;

import fr.trankilium.trankiliumutilities.globalUtils.SQLiteCore;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

import static fr.trankilium.trankiliumutilities.Main.dbPath;


public class WorldResetPlaceholder extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "worldreset";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Trankilium";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true; //
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.startsWith("time_since_reset_")) {
            String worldName = params.replace("time_since_reset_", "");
            long lastResetTime = getLastResetTime(worldName);

            long elapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(lastResetTime);
            long seconds = elapsedSeconds % 60;
            long minutes = (elapsedSeconds / 60) % 60;
            long hours = (elapsedSeconds / (60 * 60)) % 24;
            long days = (elapsedSeconds / (60 * 60 * 24)) % 30;
            long months = (elapsedSeconds / (60 * 60 * 24 * 30)) % 12;
            long years = (elapsedSeconds / (60 * 60 * 24 * 365));

            if (years > 0) {
                return String.format("%d ans et %d mois", years, months);
            } else if (months > 0) {
                return String.format("%d mois et %d jours", months, days);
            } else if (days > 0) {
                return String.format("%d jours et %d heures", days, hours);
            } else if (hours > 0) {
                return String.format("%d heures et %d minutes", hours, minutes);
            } else if (minutes > 0) {
                return String.format("%d minutes et %d secondes", minutes, seconds);
            } else {
                return String.format("%d secondes", seconds);
            }
        }

        return null;
    }

    private long getLastResetTime(String worldName) {
        SQLiteCore db = new SQLiteCore();
        db.ConnectDB(dbPath);

        long ret = 0;
        ResultSet resultSet = db.Query(String.format("SELECT LastReset FROM worldreset WHERE World = '%s'", worldName));
        try {
            if (resultSet.next())
                ret = resultSet.getLong(1);
        } catch (Exception ignored) {}
        try {
            resultSet.close();
        } catch (Exception ignored) {}
        db.DisconnectDB();
        return System.currentTimeMillis() - ret;
    }
}