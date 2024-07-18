package it.unixdevelopment.eeproxy.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import it.unixdevelopment.eecommon.type.GameType;
import it.unixdevelopment.eeproxy.EEProxy;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.aopalliance.intercept.Invocation;

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
                    String worldName = args[2];

                    try {
                        GameType.valueOf(type.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(Component.text("Invalid game type!"));
                        return;
                    }

                    instance.sendRedisMessage("create;" + type + ";" + worldName);
                    player.sendMessage(Component.text("Event created!"));
                }
            }
        }
    }
}
