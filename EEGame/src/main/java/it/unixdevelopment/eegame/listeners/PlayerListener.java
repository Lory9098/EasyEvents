package it.unixdevelopment.eegame.listeners;

import com.google.common.base.Enums;
import it.unixdevelopment.eecommon.state.GameState;
import it.unixdevelopment.eecommon.type.GameType;
import it.unixdevelopment.eegame.EEGame;
import it.unixdevelopment.eegame.data.UserData;
import it.unixdevelopment.eegame.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final EEGame instance;

    public PlayerListener(EEGame instance) {
        this.instance = instance;
    }

    @EventHandler
    public void preLogin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();

        instance.getDatabase().getUserData(uuid)
                .thenAccept(userData -> {
                    if (userData == null) {
                        userData = new UserData(uuid);
                        instance.getDatabase().saveOrCreate(
                                userData
                        );
                    }

                    instance.getPlayerManager().addUserData(uuid, userData);
                });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        try (Jedis jedis = instance.getJedisPool().getResource()) {
            String eventName = jedis.get("player:" + player.getUniqueId());

            if (eventName == null) {
                player.kickPlayer("You are not registered in any game!");
                return;
            }

            Game game = instance.getGameManager().getGameByName(eventName);

            if (game == null) {
                player.kickPlayer("Invalid game!");
                return;
            }

            if (!game.addPlayer(player)) {
                player.kickPlayer("Game is full!");
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        Game game = instance.getGameManager().getGameByPlayer(player);

        if (game == null) {
            player.kickPlayer("You are not in a game!");
            return;
        }

        e.setCancelled(game.getGameState() != GameState.PLAYING && game.getGameState() != GameState.FINISHED);
    }

}
