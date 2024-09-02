package fr.trankilium.trankiliumutilities;

import fr.trankilium.trankiliumutilities.events.InventoryClickEventHandler;
import fr.trankilium.trankiliumutilities.events.PlayerJoinEventHandler;
import fr.trankilium.trankiliumutilities.events.PlayerQuitEventHandler;
import fr.trankilium.trankiliumutilities.globalUtils.SQLiteCore;
import fr.trankilium.trankiliumutilities.utilities.rubis.RubisAdminCommand;
import fr.trankilium.trankiliumutilities.utilities.rubis.RubisCommand;
import fr.trankilium.trankiliumutilities.utilities.rubis.RubisGUICommand;
import fr.trankilium.trankiliumutilities.utilities.rubis.RubisPlaceholder;
import fr.trankilium.trankiliumutilities.utilities.shops.GuiManagers;
import fr.trankilium.trankiliumutilities.utilities.shops.ShopCommands;
import fr.trankilium.trankiliumutilities.utilities.WorldReset.ResetMinageCommand;
import fr.trankilium.trankiliumutilities.utilities.WorldReset.WorldResetPlaceholder;
import fr.trankilium.trankiliumutilities.utilities.ranks.gui.RanksGUICommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    public static Logger logger;
    public static String dbPath;
    private static Main instance;
    private static File datafolder;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();

        datafolder = new File("plugins/" + getName());
        if (!datafolder.exists()) {
            if (datafolder.mkdirs()) {
                getLogger().info("Dossier créé avec succès : " + datafolder.getAbsolutePath());
            } else {
                getLogger().severe("Échec de la création du dossier : " + datafolder.getAbsolutePath());
            }
        }
        dbPath = datafolder + "/data.db";

        /* Database */
        SQLiteCore db = new SQLiteCore();
        db.ConnectDB(dbPath);

        // Tables creation

        // Rubis
        db.Exec("CREATE TABLE IF NOT EXISTS rubis ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "player VARCHAR(255) NOT NULL UNIQUE,"
                + "balance INTEGER NOT NULL DEFAULT 0,"
                + "claims_amount INTEGER NOT NULL DEFAULT 0"
                + ");");

        // WorldReset
        db.Exec("CREATE TABLE IF NOT EXISTS worldreset ("
                + "Id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "World VARCHAR(255) NOT NULL UNIQUE,"
                + "LastReset INTEGER NOT NULL DEFAULT 0"
                + ");");

        db.DisconnectDB();

        new GuiManagers();

        /* Register Commands */
        Objects.requireNonNull(getCommand("resetminage")).setExecutor(new ResetMinageCommand());
        Objects.requireNonNull(getCommand("ranksgui")).setExecutor(new RanksGUICommand());
        Objects.requireNonNull(getCommand("shop")).setExecutor(new ShopCommands());
        Objects.requireNonNull(getCommand("rubis")).setExecutor(new RubisCommand());
        Objects.requireNonNull(getCommand("rubisadmin")).setExecutor(new RubisAdminCommand());
        Objects.requireNonNull(getCommand("rubisgui")).setExecutor(new RubisGUICommand());

        /* Register Events */
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinEventHandler(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuitEventHandler(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickEventHandler(), this);

        /* Placeholders */
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new WorldResetPlaceholder().register();
            new RubisPlaceholder().register();
            getLogger().info("Placeholders loaded successfully");
        } else {
            getLogger().warning("PlaceholderAPI not found. Placeholders won't work.");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return instance;
    }

    public static File getDatafolder() {
        return datafolder;
    }
}