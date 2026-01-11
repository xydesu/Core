package me.xydesu.core.Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.xydesu.core.Player.Class.ClassManager;
import me.xydesu.core.Player.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseManager {

    private HikariDataSource dataSource;

    public void connect(String host, String port, String database, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setJdbcUrl("jdbc:mariadb://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
        createTable();
    }

    public void disconnect() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    private void createTable() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS player_data (" +
                             "uuid VARCHAR(36) PRIMARY KEY," +
                             "level INT," +
                             "exp DOUBLE," +
                             "attribute_points INT," +
                             "strength DOUBLE," +
                             "agility DOUBLE," +
                             "intelligence DOUBLE," +
                             "vitality DOUBLE," +
                             "dexterity DOUBLE," +
                             "current_health DOUBLE," +
                             "current_mana DOUBLE," +
                             "current_stamina DOUBLE" +
                             "class VARCHAR(50)" +
                             ")")) {
                statement.executeUpdate();
            }

            // Update table for existing databases
            try (PreparedStatement statement = connection.prepareStatement(
                    "ALTER TABLE player_data ADD COLUMN IF NOT EXISTS current_health DOUBLE DEFAULT 20")) {
                statement.executeUpdate();
            } catch (SQLException ignored) {}

            try (PreparedStatement statement = connection.prepareStatement(
                    "ALTER TABLE player_data ADD COLUMN IF NOT EXISTS current_mana DOUBLE DEFAULT 20")) {
                statement.executeUpdate();
            } catch (SQLException ignored) {}

            try (PreparedStatement statement = connection.prepareStatement(
                    "ALTER TABLE player_data ADD COLUMN IF NOT EXISTS current_stamina DOUBLE DEFAULT 20")) {
                statement.executeUpdate();
            } catch (SQLException ignored) {}

            try (PreparedStatement statement = connection.prepareStatement(
                    "ALTER TABLE player_data ADD COLUMN IF NOT EXISTS class VARCHAR(50) DEFAULT 'None'")) {
                statement.executeUpdate();
            } catch (SQLException ignored) {}

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayer(Player player) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO player_data (uuid, level, exp, attribute_points, strength, agility, intelligence, vitality, dexterity, current_health, current_mana, current_stamina) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE " +
                             "level = VALUES(level), " +
                             "exp = VALUES(exp), " +
                             "attribute_points = VALUES(attribute_points), " +
                             "strength = VALUES(strength), " +
                             "agility = VALUES(agility), " +
                             "intelligence = VALUES(intelligence), " +
                             "vitality = VALUES(vitality), " +
                             "dexterity = VALUES(dexterity), " +
                             "current_health = VALUES(current_health), " +
                             "current_mana = VALUES(current_mana), " +
                             "current_stamina = VALUES(current_stamina)" +
                             "class = VALUES(class)"
                     )) {
            
            statement.setString(1, player.getUuid().toString());
            statement.setInt(2, player.getLevel());
            statement.setDouble(3, player.getExp());
            statement.setInt(4, player.getAttributePoints());
            statement.setDouble(5, player.getAllocatedStrength());
            statement.setDouble(6, player.getAllocatedAgility());
            statement.setDouble(7, player.getAllocatedIntelligence());
            statement.setDouble(8, player.getAllocatedVitality());
            statement.setDouble(9, player.getAllocatedDexterity());
            statement.setDouble(10, player.getCurrentHealth());
            statement.setDouble(11, player.getCurrentMana());
            statement.setDouble(12, player.getCurrentStamina());

            statement.setString(13, player.getPlayerClass().className());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayer(Player player) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM player_data WHERE uuid = ?")) {
            
            statement.setString(1, player.getUuid().toString());
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                player.setLevel(resultSet.getInt("level"));
                player.setExp(resultSet.getDouble("exp"));
                player.setAttributePoints(resultSet.getInt("attribute_points"));
                player.setAllocatedStrength(resultSet.getDouble("strength"));
                player.setAllocatedAgility(resultSet.getDouble("agility"));
                player.setAllocatedIntelligence(resultSet.getDouble("intelligence"));
                player.setAllocatedVitality(resultSet.getDouble("vitality"));
                player.setAllocatedDexterity(resultSet.getDouble("dexterity"));
                player.setCurrentHealth(resultSet.getDouble("current_health"));
                player.setCurrentMana(resultSet.getDouble("current_mana"));
                // Check if column exists (for backward compatibility if not using ALTER TABLE correctly, but we did)
                try {
                    player.setCurrentStamina(resultSet.getDouble("current_stamina"));
                } catch (SQLException ignored) {
                    // Column might not exist yet if migration failed or something
                }

                String className = resultSet.getString("class");
                ClassManager _class = ClassManager.get(className);
                if (_class != null) {
                    player.setPlayerClass(_class);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
