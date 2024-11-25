package it.unixdevelopment.eeproxy;

import com.github.retrooper.packetevents.PacketEvents;
import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import io.github.retrooper.packetevents.velocity.factory.VelocityPacketEventsBuilder;
import lombok.Getter;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

@Plugin(
        id = "eeproxy",
        name = "EEProxy",
        version = "1.0"
)
@Getter
public class EEProxy {

    private static EEProxy instance;
    private ProxyServer proxyServer;
    @Inject
    private Logger logger;
    private JedisPool jedisPool;
    private static YamlDocument config;
    private Path dataDirectory;
    private PluginContainer pluginContainer;

    @Inject
    public EEProxy(ProxyServer proxyServer, PluginContainer pluginContainer, Logger logger, @DataDirectory Path dataDirectory) {
        instance = this;
        this.proxyServer = proxyServer;
        this.pluginContainer = pluginContainer;
        this.logger = logger;

        try {
            config = YamlDocument.create(new File(dataDirectory.toFile(), "config.yml"),
                    Objects.requireNonNull(getClass().getResourceAsStream("/config.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version"))
                            .setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS)
                            .build()
            );

            config.update();
            config.save();
        } catch (Exception e) {
            logger.error("Failed to load config.yml", e);
        }

        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        PacketEvents.setAPI(VelocityPacketEventsBuilder.build(proxyServer, pluginContainer, logger, dataDirectory));

        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                .checkForUpdates(true);
        PacketEvents.getAPI().load();

        this.jedisPool = new JedisPool(
                config.getString("redis.host"),
                Integer.parseInt(config.getString("redis.port"))
        );

    }

    public void sendRedisMessage(String message) {
        try (Jedis jedis = getJedisPool().getResource()) {
            jedis.publish("easyevents", message);
        }
    }
}
