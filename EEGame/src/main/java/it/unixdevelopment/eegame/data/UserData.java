package it.unixdevelopment.eegame.data;

import it.unixdevelopment.eecommon.type.GameType;
import lombok.Data;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class UserData {

    private final UUID uuid;
    //                    Tipo di game,         Statistica,  valore
    private final HashMap<GameType,     HashMap<String,      Integer>> statistics = new HashMap<>();

    public UserData(UUID uuid) {
        this.uuid = uuid;
    }

    public UserData(UUID uuid, HashMap<GameType, HashMap<String, Integer>> statistics) {
        this.uuid = uuid;
        this.statistics.putAll(statistics);
    }

    public int getStatistic(GameType gameType, String statistic) {
        if (!statistics.containsKey(gameType)) {
            return 0;
        }

        if (!statistics.get(gameType).containsKey(statistic)) {
            return 0;
        }

        return statistics.get(gameType).get(statistic);
    }

    public void setStatistic(GameType gameType, String statistic, int value) {
        if (!statistics.containsKey(gameType)) {
            statistics.put(gameType, new HashMap<>());
        }

        statistics.get(gameType).put(statistic, value);
    }

}
