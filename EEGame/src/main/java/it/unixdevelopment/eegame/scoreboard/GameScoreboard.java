package it.unixdevelopment.eegame.scoreboard;

import it.unixdevelopment.eegame.EEGame;
import it.unixdevelopment.eegame.game.Game;
import it.unixdevelopment.eegame.utils.color.ChatUtil;
import lombok.RequiredArgsConstructor;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.scoreboard.Scoreboard;
import me.neznamy.tab.api.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@RequiredArgsConstructor
public class GameScoreboard extends BukkitRunnable {

    private final EEGame instance;
    private final ScoreboardManager scoreboardManager;

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(this::refreshScoreboard);
    }

    public void refreshScoreboard(Player player) {
        Game game = instance.getGameManager().getGameByPlayer(player);

        final String scoreboardTitle;
        final List<String> scoreboardLines;

        if (game == null) {
            scoreboardTitle = ChatUtil.papiColor(player, instance.getConfig().getString("graphics.null.scoreboard.title"));
            scoreboardLines = ChatUtil.papiColor(player, instance.getConfig().getStringList("graphics.null.scoreboard.lines"));
        } else {
            scoreboardTitle = ChatUtil.papiColor(player, instance.getConfig().getString("graphics." + game.getGameType().name().toLowerCase() + ".scoreboard.title"));
            scoreboardLines = ChatUtil.papiColor(player, instance.getConfig().getStringList("graphics." + game.getGameType().name().toLowerCase() + ".scoreboard.lines"));
        }

        TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());

        if (tabPlayer == null) {
            return;
        }

        Scoreboard scoreboard = scoreboardManager.createScoreboard("scoreboard", scoreboardTitle, scoreboardLines);
        scoreboardManager.showScoreboard(tabPlayer, scoreboard);
    }

}
