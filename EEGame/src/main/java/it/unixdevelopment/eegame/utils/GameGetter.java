package it.unixdevelopment.eegame.utils;

import it.unixdevelopment.eecommon.type.GameType;
import it.unixdevelopment.eegame.game.Game;

public class GameGetter {

    public static Class<? extends Game> getGame(GameType gameType) {
        switch (gameType) {


            default -> throw new IllegalArgumentException("Invalid game type!");
        }
    }

}
