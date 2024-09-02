package fr.trankilium.trankiliumutilities.utilities.rubis;

import fr.trankilium.trankiliumutilities.globalUtils.SQLiteCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

import static fr.trankilium.trankiliumutilities.Main.dbPath;

public class RubisAdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /rubisadmin <add|remove|set|get> <player> [amount] [silent]");
            return true;
        }

        String action = args[0];
        String playerName = args[1].toLowerCase();
        int amount = 0;
        boolean silent = false;

        // Check for optional 'silent' argument
        if (args.length > 2 && args[args.length - 1].equalsIgnoreCase("silent")) {
            silent = true;
        }

        // Validate amount for 'add' and 'set' actions
        if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("set") || action.equalsIgnoreCase("remove")) {
            if (args.length < 3 || (args.length == 3 && silent)) {
                if (!silent) sender.sendMessage("§cUsage: /rubisadmin <add|set> <player> <amount> [silent]");
                return true;
            }

            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                if (!silent) sender.sendMessage("§cLe montant doit être un nombre entier.");
                return true;
            }
        }

        SQLiteCore db = new SQLiteCore();
        if (db.ConnectDB(dbPath)) {
            String sql;
            switch (action.toLowerCase()) {
                case "add":
                    sql = String.format("UPDATE rubis SET balance = balance + %d WHERE player = '%s'", amount, playerName);
                    break;
                case "remove":
                    sql = String.format("UPDATE rubis SET balance = balance - %d WHERE player = '%s'", amount, playerName);
                    break;
                case "set":
                    sql = String.format("INSERT OR REPLACE INTO rubis (player, balance) VALUES ('%s', %d)", playerName, amount);
                    break;
                case "get":
                    sql = String.format("SELECT balance FROM rubis WHERE player = '%s'", playerName);
                    ResultSet rs = db.Query(sql);
                    try {
                        if (rs != null && rs.next()) {
                            int balance = rs.getInt("balance");
                            if (!silent) sender.sendMessage(String.format("§aLe joueur %s a %d rubis.", playerName, balance));
                        } else {
                            if (!silent) sender.sendMessage("§cLe joueur n'a pas de rubis enregistrés.");
                        }
                    } catch (SQLException e) {
                        if (!silent) sender.sendMessage("§cUne erreur est survenue lors de la récupération des rubis.");
                        e.printStackTrace();
                    }
                    db.DisconnectDB();
                    return true;
                default:
                    if (!silent) sender.sendMessage("§cAction invalide. Utilisez 'add', 'set' ou 'get'.");
                    db.DisconnectDB();
                    return true;
            }

            boolean success = db.Exec(sql);
            if (success) {
                if (!silent) sender.sendMessage("§aRubis mis à jour avec succès.");
            } else {
                if (!silent) sender.sendMessage("§cUne erreur est survenue lors de la mise à jour des rubis.");
            }

            db.DisconnectDB();
        } else {
            if (!silent) sender.sendMessage("§cImpossible de se connecter à la base de données.");
        }
        return true;
    }
}