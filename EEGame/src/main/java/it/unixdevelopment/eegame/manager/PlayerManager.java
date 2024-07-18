package it.unixdevelopment.eegame.manager;

import it.unixdevelopment.eegame.data.UserData;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private final HashMap<UUID, UserData> players = new HashMap<>();

    public UserData getUserData(UUID uuid) {
        return players.get(uuid);
    }

    public void addUserData(UUID uuid, UserData userData) {
        players.put(uuid, userData);
    }

    public void removeUserData(UUID uuid) {
        players.remove(uuid);
    }

    public boolean hasUserData(UUID uuid) {
        return players.containsKey(uuid);
    }

}
