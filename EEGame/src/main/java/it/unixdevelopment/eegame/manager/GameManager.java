package it.unixdevelopment.eegame.manager;

import it.unixdevelopment.eecommon.type.GameType;
import it.unixdevelopment.eegame.game.Game;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GameManager {

    private HashMap<GameType, Set<Game>> games = new HashMap<>();

    public void addGame(GameType gameType, Game game) {
        if (!games.containsKey(gameType)) {
            games.put(gameType, new HashSet<>());
        }

        games.get(gameType).add(game);
    }

    public void removeGame(GameType gameType, Game game) {
        if (games.containsKey(gameType)) {
            games.get(gameType).remove(game);
        }
    }

    public Set<Game> getGames(GameType gameType) {
        return games.get(gameType);
    }

    public Game getGameByName(String eventName) {
        return games.values().stream()
                .flatMap(Set::stream)
                .filter(game -> game.getName().equals(eventName))
                .findFirst()
                .orElse(null);
    }

    public Game getGameByPlayer(Player player) {
        return games.values().stream()
                .flatMap(Set::stream)
                .filter(game -> game.getPlayers().contains(player))
                .findFirst()
                .orElse(null);
    }
}
