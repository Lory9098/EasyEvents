package it.unixdevelopment.eegame.countdown;

import it.unixdevelopment.eecommon.countdown.ICountdown;
import it.unixdevelopment.eegame.EEGame;
import it.unixdevelopment.eegame.game.Game;

public class Countdown extends ICountdown {
    private Game game;

    public Countdown(Game game, int duration) {
        super(EEGame.getInstance(), duration);
        this.game = game;
    }

    @Override
    public void tick(int currentSecond) {
        if (currentSecond == 0) {
            game.start();
            stop();
            return;
        }

        if (game.getCountdownMsgCondition().test(currentSecond)) {
            game.broadcastCountdownMessage(currentSecond);
        }
    }
}
