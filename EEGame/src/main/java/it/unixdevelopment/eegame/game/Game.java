package it.unixdevelopment.eegame.game;

import it.unixdevelopment.eecommon.state.GameState;
import it.unixdevelopment.eecommon.type.GameType;
import it.unixdevelopment.eegame.EEGame;
import it.unixdevelopment.eegame.countdown.Countdown;
import it.unixdevelopment.eegame.utils.arena.ArenaConfigurationUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

@Getter @Setter
public abstract class Game {
    private final String name;
    private final GameType gameType;
    private GameState gameState = GameState.WAITING;
    private final Set<Player> players = new HashSet<>();
    private final int countdownSeconds;
    private Countdown countdown;
    private final Location spawnLocation;

    public Game(String name, GameType gameType, int countdownSeconds, Location spawnLocation) {
        this.name = name;
        this.gameType = gameType;
        this.countdownSeconds = countdownSeconds;
        this.spawnLocation = spawnLocation;

        this.countdown = new Countdown(this, countdownSeconds);

        onCreate();
    }

    public boolean addPlayer(Player player) {
        if (gameState != GameState.WAITING && gameState != GameState.COUNTDOWN) {
            return false;
        }

        if (players.size() >= gameType.getMaxPlayers()) {
            return false;
        }

        players.add(player);

        // qui si potrebbe notificare l'entrata del player

        return true;
    }

    public boolean removePlayer(Player player) {
        if (players.contains(player)) {
            players.remove(player);

            // qui si potrebbe notificare l'uscita del player

            return true;
        }

        return false;
    }

    public void startCountdown() {
        players.forEach(player -> player.teleport(spawnLocation));

        gameState = GameState.COUNTDOWN;

        if (countdown == null) {
            countdown = new Countdown(this, countdownSeconds);
        }

        countdown.start();
    }

    public void stopCountdown() {
        if (countdown != null) {
            countdown.stop();
        }

        countdown = null;
    }

    public void start() {
        gameState = GameState.PLAYING;
        countdown = null;
        EEGame.getInstance().getTimeManager().start(this);
        startGame();
    }

    public void finishGame() {
        EEGame.getInstance().getTimeManager().stop(this);
    }

    /**
     * Questo metodo restituisce il nome del gioco
     * @return il nome del gioco
     * */
    public abstract String getName();

    /**
     * Questo codice viene eseguito quando l'arena viene creata
     * */
    public abstract void onCreate();

    /**
     * Questo codice viene eseguito quando il gioco inizia
     * */
    public abstract void startGame();

    /**
     * Questo metodo restituisce una condizione che deve essere soddisfatta per inviare un messaggio di countdown
     * @return la condizione
     * */
    public abstract Predicate<Integer> getCountdownMsgCondition();

    /**
     * Questo metodo invia un messaggio di countdown
     * @param currentSecond il secondo corrente del countdown
     * */
    public abstract void broadcastCountdownMessage(int currentSecond);

    public void setWorld(World world) {
        World oldWorld = spawnLocation.getWorld();
        try {
            ArenaConfigurationUtils.editSpecificArena(getName(), "spawnLocation.world", world.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        spawnLocation.setWorld(world);
        onWorldChange(oldWorld, world);
    }

    public abstract void onWorldChange(World oldWorld, World newWorld);
}
