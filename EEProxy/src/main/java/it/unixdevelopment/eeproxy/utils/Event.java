package it.unixdevelopment.eeproxy.utils;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class Event {

    private Set<String> events = new HashSet<>();

    public void addEvent(String event) {
        events.add(event);
    }

    public void removeEvent(String event) {
        events.remove(event);
    }

    public static boolean isEventEnabled(String arenaName) {
        return events.contains(arenaName);
    }
}
