package it.unixdevelopment.eegame.hook;

import it.unixdevelopment.eegame.EEGame;
import it.unixdevelopment.eegame.game.Game;
import lombok.AllArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

@AllArgsConstructor
public class PlaceholderHook extends PlaceholderExpansion {
    private final EEGame instance;

    @Override
    public @NotNull String getIdentifier() {
        return "easyevents";
    }

    @Override
    public @NotNull String getAuthor() {
        return "NettyChannell";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(final Player player, @NotNull final String params) {

        Game game = instance.getGameManager().getGameByPlayer(player);
        if (game == null) {
            return "0";
        }

        switch (params) {
            case "time":
                long elapsedTimeSinceStart = instance.getTimeManager().getElapsedTime(game) / 1000;

                long minutes = elapsedTimeSinceStart / 60;
                long seconds = elapsedTimeSinceStart % 60;

                return String.format("%d:%02d", minutes, seconds);
            case "event":
                return game.getName();
        }

        return "";
    }

}
