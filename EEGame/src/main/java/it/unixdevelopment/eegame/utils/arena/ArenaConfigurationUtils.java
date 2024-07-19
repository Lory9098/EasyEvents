package it.unixdevelopment.eegame.utils.arena;

import it.unixdevelopment.eegame.EEGame;
import it.unixdevelopment.eegame.game.Game;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

@UtilityClass
public class ArenaConfigurationUtils {

    public SavingResult saveArena(Game game, boolean update) throws IOException {
        ConfigurationSection section = EEGame.getInstance().getArenasConfig().getConfigurationSection("arenas");

        if (section == null) {
            EEGame.getInstance().getArenasConfig().createSection("arenas");

            return saveArena(game, update);
        }

        if (!update && section.contains(game.getName())) {
            return SavingResult.ALREADY_EXISTS;
        }

        section.createSection(game.getName());

        FileConfiguration config = EEGame.getInstance().getArenasConfig();
        config.set("arenas." + game.getName() + ".gameType", game.getGameType().name());
        config.set("arenas." + game.getName() + ".countdownSeconds", game.getCountdownSeconds());
        config.set("arenas." + game.getName() + ".spawnLocation.world", game.getSpawnLocation().getWorld().getName());
        config.set("arenas." + game.getName() + ".spawnLocation.x", game.getSpawnLocation().getX());
        config.set("arenas." + game.getName() + ".spawnLocation.y", game.getSpawnLocation().getY());
        config.set("arenas." + game.getName() + ".spawnLocation.z", game.getSpawnLocation().getZ());
        config.set("arenas." + game.getName() + ".spawnLocation.yaw", game.getSpawnLocation().getYaw());
        config.set("arenas." + game.getName() + ".spawnLocation.pitch", game.getSpawnLocation().getPitch());

        EEGame.getInstance().getArenasConfig().save("arenas.yml");
        return SavingResult.SUCCESS;
    }

    public static void editSpecificArena(String name, String path, String newValue) throws IOException {
        FileConfiguration config = EEGame.getInstance().getArenasConfig();
        config.set("arenas." + name + "." + path, newValue);
        config.save("arenas.yml");
    }

    public enum SavingResult {
        SUCCESS,
        ALREADY_EXISTS
    }

}
