package fr.trankilium.trankiliumutilities.utilities.rubis;

import fr.trankilium.trankiliumutilities.globalUtils.SQLiteCore;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

import static fr.trankilium.trankiliumutilities.Main.dbPath;

public class RubisPlaceholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "rubis";
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
        if (params.equalsIgnoreCase("balance")) {
            SQLiteCore db = new SQLiteCore();
            if (db.ConnectDB(dbPath) && player.getName() != null) {
                String sql = String.format("SELECT balance FROM rubis WHERE player = '%s'", player.getName().toLowerCase());
                ResultSet resultSet = db.Query(sql);

                try {
                    if (resultSet != null && resultSet.next()) {
                        // Obtenir le nombre de rubis
                        int balance = resultSet.getInt("balance");
                        return String.valueOf(balance);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    // Déconnecter la base de données
                    db.DisconnectDB();
                }
            }
        }
        return null;
    }
}
