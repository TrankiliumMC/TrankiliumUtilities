package fr.trankilium.trankiliumutilities;

import fr.trankilium.trankiliumutilities.events.InventoryClickEventHandler;
import fr.trankilium.trankiliumutilities.events.PlayerJoinEventHandler;
import fr.trankilium.trankiliumutilities.events.PlayerQuitEventHandler;
import fr.trankilium.trankiliumutilities.globalUtils.SQLiteCore;
import fr.trankilium.trankiliumutilities.utilities.WorldReset.ResetMinageCommand;
import fr.trankilium.trankiliumutilities.utilities.WorldReset.WorldResetPlaceholder;
import fr.trankilium.trankiliumutilities.utilities.ranks.gui.RanksGUICommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class TrankiliumUtilities extends JavaPlugin {

    public static Logger logger;
    public static String dbPath;
    public static FileConfiguration fileConfiguration;

    @Override
    public void onEnable() {
        logger = getLogger();
        dbPath = "plugins/TrankiliumUtilities/data.db";
        this.saveDefaultConfig();
        fileConfiguration = this.getConfig();


        /* Database */
        SQLiteCore db = new SQLiteCore();
        db.ConnectDB(dbPath);

        // Tables creation

        // WorldReset
        db.Exec("CREATE TABLE IF NOT EXISTS worldreset ("
                + "Id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "World VARCHAR(255) NOT NULL UNIQUE,"
                + "LastReset INTEGER NOT NULL DEFAULT 0"
                + ");");

        db.DisconnectDB();

        /* Register Commands */
        Objects.requireNonNull(getCommand("resetminage")).setExecutor(new ResetMinageCommand());
        Objects.requireNonNull(getCommand("ranksgui")).setExecutor(new RanksGUICommand());

        /* Register Events */
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinEventHandler(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuitEventHandler(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickEventHandler(), this);

        /* Placeholders */
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new WorldResetPlaceholder().register();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
