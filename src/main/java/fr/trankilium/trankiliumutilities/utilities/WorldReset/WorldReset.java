package fr.trankilium.trankiliumutilities.utilities.WorldReset;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import fr.trankilium.trankiliumutilities.globalUtils.SQLiteCore;
import org.bukkit.Bukkit;

import static fr.trankilium.trankiliumutilities.Main.dbPath;
import static fr.trankilium.trankiliumutilities.Main.logger;


public class WorldReset {
    public static void resetWorld(String worldName) {
        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        if (core != null) {
            MVWorldManager worldManager = core.getMVWorldManager();
            worldManager.regenWorld(worldName, true, true, "", true);
            logger.info(String.format("World '%s' has been reseted succesfully.", worldName));

            // Database reset time save
            long time = System.currentTimeMillis();
            SQLiteCore db = new SQLiteCore();
            db.ConnectDB(dbPath);
            db.Exec(String.format("INSERT OR REPLACE INTO worldreset (World, LastReset) VALUES ('%s', %d);", worldName, time));
            db.DisconnectDB();
        } else {
            logger.severe(String.format("Unable to reset world '%s'. MultiverseCore not found.", worldName));
        }
    }
}
