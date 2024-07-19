package it.unixdevelopment.eeproxy.commands;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.player.User;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import it.unixdevelopment.eecommon.type.GameType;
import it.unixdevelopment.eeproxy.EEProxy;
import it.unixdevelopment.eeproxy.utils.Event;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

@RequiredArgsConstructor
public class EventCMD implements SimpleCommand {
    private final EEProxy instance;

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) return;

        Player player = (Player) invocation;
        String[] args = invocation.arguments();

        if (args.length == 0) {
            player.sendMessage(Component.text("Invalid usage!"));
            return;
        }

        if (args.length == 3) {
            switch (args[0]) {
                case "create" -> {
                    String type = args[1];
                    String arenaName = args[2];
                    String worldName = args[3];

                    try {
                        GameType.valueOf(type.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(Component.text("Invalid game type!"));
                        return;
                    }

                    instance.sendRedisMessage("create;" + type + ";" + arenaName + ";" + worldName);
                    player.sendMessage(Component.text("Event created!"));
                }
                case "set-world" -> {
                    String arenaName = args[1];
                    String worldName = args[2];

                    instance.sendRedisMessage("set-world;" + arenaName + ";" + worldName);
                    player.sendMessage(Component.text("World set!"));
                }
                case "set-delay" -> {
                    String arenaName = args[1];
                    int delay;

                    try {
                        delay = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(Component.text("Invalid delay!"));
                        return;
                    }

                    instance.sendRedisMessage("set-delay;" + arenaName + ";" + delay);
                    player.sendMessage(Component.text("Delay set!"));
                }
            }
        } else if (args.length == 2) {
            switch (args[0]) {
                case "set-kit" -> {
                    String arenaName = args[1];


                }
                case "enable" -> {
                    String arenaName = args[1];

                    instance.getProxyServer().getAllServers().forEach(registeredServer -> {
                        registeredServer.getPlayersConnected().forEach(p -> {
                            p.sendMessage(Component.text("Event " + arenaName + " is now enabled!"));
                        });
                    });

                    Event.addEvent(arenaName);
                    player.sendMessage(Component.text("Event enabled!"));
                }
                case "start" -> {
                    String arenaName = args[1];

                    if (!Event.isEventEnabled(arenaName)) {
                        player.sendMessage(Component.text("Event is not enabled!"));
                        return;
                    }

                    instance.sendRedisMessage("start;" + arenaName);
                    player.sendMessage(Component.text("Event started!"));
                }
            }
        }
    }
}
