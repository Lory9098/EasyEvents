package it.unixdevelopment.eegame.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.unixdevelopment.eegame.data.UserData;
import it.unixdevelopment.eegame.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Database {

    private final HikariDataSource dataSource;

    public Database(String host, String port, String database, String username, String password) {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
        createTables();
    }

    private void createTables() {
        String query = "CREATE TABLE IF NOT EXISTS `statistics` (id INT AUTO_INCREMENT PRIMARY KEY, uuid VARCHAR(36), player BLOB)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveOrCreate(@NotNull UserData userData) {
        CompletableFuture.runAsync(() -> {
            String query = "INSERT INTO `statistics` (name, uuid) VALUES (?, ?) ON DUPLICATE KEY UPDATE player = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, userData.getUuid().toString());
                statement.setBytes(2, Serializer.serialize(userData));
                statement.setBytes(3, Serializer.serialize(userData));
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<UserData> getUserData(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT * FROM `statistics` WHERE uuid = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, uuid.toString());
                ResultSet rs = statement.executeQuery();

                return Serializer.deserialize(rs.getBytes("player"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

}
