package it.unixdevelopment.eecommon.countdown;

import it.unixdevelopment.eecommon.tickable.Tickable;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class ICountdown extends BukkitRunnable implements Tickable {

    private final JavaPlugin javaPlugin;
    private int duration;

    public ICountdown(JavaPlugin javaPlugin, int duration) {
        this.javaPlugin = javaPlugin;
        this.duration = duration;
    }

    @Override
    public void start() {
        this.runTaskTimerAsynchronously(javaPlugin, 0, 20);
    }

    public abstract void tick(int currentSecond);

    @Override
    public void run() {
        tick(duration--);
    }

    public void stop() {
        this.cancel();
    }

}
