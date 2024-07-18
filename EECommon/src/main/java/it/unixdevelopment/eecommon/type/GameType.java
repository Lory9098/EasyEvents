package it.unixdevelopment.eecommon.type;

import lombok.Data;
import lombok.Getter;

@Getter
public enum GameType {

    ;

    private final int minPlayers, maxPlayers;

    GameType(int minPlayers, int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

}
