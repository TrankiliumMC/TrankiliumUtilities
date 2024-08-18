package fr.trankilium.trankiliumutilities.utilities.ranks.gui;

import fr.trankilium.trankiliumutilities.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class RankFile {

    private FileConfiguration config;
    private final File file;

    public RankFile() {
        file = new File(Main.getDatafolder(), "rank.yml");
        try {
            if(!file.createNewFile()) {
                YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

                yml.set("ranks.gui.price.divin", 60000);
                yml.set("ranks.gui.price.celeste", 80000);
                yml.set("ranks.gui.price.transcendant", 100000);
                yml.set("ranks.gui.price.vip", 13.99);
                yml.set("ranks.gui.vip-discount", 20);
                yml.save(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getInt(String path) {
        return YamlConfiguration.loadConfiguration(file).getInt(path);
    }

    public double getDouble(String path) {
        return YamlConfiguration.loadConfiguration(file).getDouble(path);
    }
}
