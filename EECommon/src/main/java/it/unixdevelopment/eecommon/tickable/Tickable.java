package it.unixdevelopment.eecommon.tickable;

public interface Tickable {

    void start();

    void tick(int currentSecond);

    void stop();

}
