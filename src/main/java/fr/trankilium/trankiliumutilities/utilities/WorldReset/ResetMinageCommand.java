package fr.trankilium.trankiliumutilities.utilities.WorldReset;

import fr.trankilium.trankiliumutilities.globalUtils.SQLiteCore;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fr.trankilium.trankiliumutilities.Main.dbPath;
import static fr.trankilium.trankiliumutilities.Main.logger;


public class ResetMinageCommand implements CommandExecutor {
    private final String worldName = "minage";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("worldreset.resetminage")) {
            WorldReset.resetWorld(worldName);
            SQLiteCore db = new SQLiteCore();
            db.ConnectDB(dbPath);
            if (!db.ConnectDB(dbPath)) {
                sender.sendMessage("§cErreur de connexion à la base de données.");
                return false;
            }
            logger.info(" "+ db.Exec(String.format("INSERT OR REPLACE INTO worldreset (World, LastReset) VALUES ('%s', %d);", worldName, System.currentTimeMillis())));
            db.DisconnectDB();
        } else {
            sender.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande.");
        }
        return true;
    }
}
