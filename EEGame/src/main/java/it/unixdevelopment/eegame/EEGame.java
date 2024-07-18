package it.unixdevelopment.eegame;

import it.unixdevelopment.eegame.database.Database;
import it.unixdevelopment.eegame.hook.PlaceholderHook;
import it.unixdevelopment.eegame.manager.GameManager;
import it.unixdevelopment.eegame.manager.PlayerManager;
import it.unixdevelopment.eegame.manager.TimeManager;
import it.unixdevelopment.eegame.redis.RedisListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
public final class EEGame extends JavaPlugin {

    @Getter
    private static EEGame instance;
    private PlayerManager playerManager;
    private GameManager gameManager;
    private TimeManager timeManager;
    private Database database;
    private JedisPool jedisPool;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        playerManager = new PlayerManager();
        gameManager = new GameManager();
        timeManager = new TimeManager();

        database = new Database(
                getConfig().getString("database.host"),
                getConfig().getString("database.port"),
                getConfig().getString("database.database"),
                getConfig().getString("database.username"),
                getConfig().getString("database.password")
        );

        this.jedisPool = new JedisPool(
                getConfig().getString("redis.host"),
                Integer.parseInt(getConfig().getString("redis.port"))
        );

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.subscribe(new RedisListener(), "easyevents");
        }

        new PlaceholderHook(this).register();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
