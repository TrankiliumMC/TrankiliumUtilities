package fr.trankilium.trankiliumutilities.utilities.rubis;

import fr.trankilium.trankiliumutilities.globalUtils.SQLiteCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

import static fr.trankilium.trankiliumutilities.Main.dbPath;

public class RubisCommand implements CommandExecutor {


    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // Vérifier si le sender est un joueur
        if (sender instanceof Player player) {
            String playerName = player.getName().toLowerCase();

            // Connecter à la base de données
            SQLiteCore db = new SQLiteCore();
            if (db.ConnectDB(dbPath)) {
                String sql = String.format("SELECT balance FROM rubis WHERE player = '%s'", playerName);
                ResultSet resultSet = db.Query(sql);

                try {
                    if (resultSet != null && resultSet.next()) {
                        // Obtenir le nombre de rubis
                        int balance = resultSet.getInt("balance");
                        player.sendMessage("§6§l[Rubis] §eTu as §a" + balance + " Rubis§e.");
                    } else {
                        player.sendMessage("§cUne erreur est survenue.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    player.sendMessage("§cUne erreur est survenue.");
                } finally {
                    // Déconnecter la base de données
                    db.DisconnectDB();
                }
            } else {
                player.sendMessage("§cUne erreur est survenue.");
            }
        } else {
            sender.sendMessage("§cCette commande peut seulement être exécutée par un joueur.");
        }
        return true;
    }
}
