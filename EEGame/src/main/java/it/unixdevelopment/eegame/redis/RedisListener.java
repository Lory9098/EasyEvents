package it.unixdevelopment.eegame.redis;

import it.unixdevelopment.eecommon.type.GameType;
import it.unixdevelopment.eegame.EEGame;
import it.unixdevelopment.eegame.game.Game;
import it.unixdevelopment.eegame.utils.GameGetter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import redis.clients.jedis.JedisPubSub;

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
                String worldName = args[2];

                World world = Bukkit.createWorld(
                        WorldCreator
                                .name(worldName)
                );

                GameType gameType = GameType.valueOf(type);

                Class<? extends Game> clazz = GameGetter.getGame(gameType);

                try {
                    Constructor<? extends Game> constructor = clazz.getConstructor(String.class, GameType.class, int.class, Location.class);
                    Game game = constructor.newInstance(worldName, gameType, 30, new Location(world, 0, 0, 0));

                    instance.getGameManager().addGame(gameType, game);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            case "start" -> {
                // Start event
            }
            case "stop" -> {
                // Stop event
            }
        }
    }

}
