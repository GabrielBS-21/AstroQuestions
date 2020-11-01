package me.gabriel.astroquestions.sql.connection.mysql;

import com.zaxxer.hikari.HikariDataSource;
import me.gabriel.astroquestions.AstroQuestions;
import me.gabriel.astroquestions.sql.connection.SQLConnection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Henry Fábio
 * Github: https://github.com/HenryFabio
 */
public final class MySQLConnection implements SQLConnection {

    private final HikariDataSource dataSource = new HikariDataSource();

    @Override
    public void configure(ConfigurationSection section) {

        dataSource.setUsername(section.getString("username"));
        dataSource.setPassword(section.getString("password"));
        dataSource.setJdbcUrl(
                "jdbc:mysql://" + section.getString("address") + "/" + section.getString("database")
        );

        dataSource.setMaximumPoolSize(3);
        dataSource.setConnectionTimeout(30000);
        dataSource.setMaxLifetime(1800000);
        dataSource.setIdleTimeout(60000);
        dataSource.setMinimumIdle(2);

    }

    @Override
    public Connection findConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException t) {
            t.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(AstroQuestions.getInstance());
        }

        return null;
    }

}
