package it.unixdevelopment.eecommon.type;

import lombok.Data;
import lombok.Getter;

@Getter
public enum GameType {

    ;

    private final int maxPlayers;

    GameType(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

}
