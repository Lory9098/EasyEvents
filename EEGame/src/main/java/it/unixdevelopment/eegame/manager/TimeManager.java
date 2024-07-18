package it.unixdevelopment.eegame.manager;

import it.unixdevelopment.eegame.game.Game;

import java.util.HashMap;

public class TimeManager {

    private final HashMap<Game, Long> startTimes = new HashMap<>();

    public void start(Game game) {
        startTimes.put(game, System.currentTimeMillis());
    }

    public void stop(Game game) {
        startTimes.remove(game);
    }

    public long getElapsedTime(Game game) {
        return System.currentTimeMillis() - startTimes.get(game);
    }

    public boolean isRunning(Game game) {
        return startTimes.containsKey(game);
    }

}
