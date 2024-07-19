package it.unixdevelopment.eegame.redis;

import it.unixdevelopment.eecommon.type.GameType;
import it.unixdevelopment.eegame.EEGame;
import it.unixdevelopment.eegame.game.Game;
import it.unixdevelopment.eegame.utils.GameGetter;
import it.unixdevelopment.eegame.utils.arena.ArenaConfigurationUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor
public class RedisListener extends JedisPubSub {

    private final EEGame instance;

    @Override
    public void onMessage(String channel, String message) {
        String[] args = message.split(";");
        switch (args[0]) {
            case "create" -> {
                String type = args[1].toUpperCase();
                String arenaName = args[2];
                String worldName = args[3];

                World world = Bukkit.createWorld(
                        WorldCreator
                                .name(worldName)
                );

                GameType gameType = GameType.valueOf(type);

                Class<? extends Game> clazz = GameGetter.getGame(gameType);

                try {
                    Constructor<? extends Game> constructor = clazz.getConstructor(String.class, GameType.class, int.class, Location.class);
                    Game game = constructor.newInstance(arenaName, gameType, 30, new Location(world, 0, 0, 0));

                    instance.getGameManager().addGame(gameType, game);

                    ArenaConfigurationUtils.SavingResult savingResult = ArenaConfigurationUtils.saveArena(game, false);

                    switch (savingResult) {
                        case SUCCESS -> {
                            Bukkit.getLogger().info("Arena " + arenaName + " created successfully!");
                        }
                        case ALREADY_EXISTS -> {
                            Bukkit.getLogger().severe("Arena " + arenaName + " already exists!");
                        }
                    }
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "set-world" -> {
                String arenaName = args[1];
                String worldName = args[2];

                Game game = instance.getGameManager().getGameByName(arenaName);

                if (game == null) {
                    Bukkit.getLogger().severe("Invalid game!");
                    return;
                }

                World world = Bukkit.createWorld(
                        WorldCreator
                                .name(worldName)
                );

                game.setWorld(world);
            }
            case "set-delay" -> {

            }
            case "start" -> {
                String arenaName = args[1];

                Game game = instance.getGameManager().getGameByName(arenaName);

                if (game == null) {
                    Bukkit.getLogger().severe("Invalid game!");
                    return;
                }

                game.startCountdown();
            }
        }
    }

}
