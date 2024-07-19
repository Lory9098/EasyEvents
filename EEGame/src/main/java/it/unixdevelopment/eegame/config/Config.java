package it.unixdevelopment.eegame.config;

import java.io.File;
import java.io.IOException;

import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Config {
    private File file;
    private FileConfiguration fileConfiguration;

    public Config(JavaPlugin javaPlugin, String path) {
        File configFile = new File(javaPlugin.getDataFolder(), path);
        this.file = configFile;
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            javaPlugin.saveResource(path, false);
        }

        FileConfiguration configuration = new YamlConfiguration();

        try {
            configuration.load(configFile);
        } catch (InvalidConfigurationException | IOException ex) {
            ex.printStackTrace();
        }

        this.fileConfiguration = configuration;
    }

    public void reload() {
        try {
            this.fileConfiguration.load(this.file);
        } catch (InvalidConfigurationException | IOException ex) {
            ex.printStackTrace();
        }

    }

    public void save() {
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
