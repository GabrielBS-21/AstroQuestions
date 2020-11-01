package me.gabriel.astroquestions.sql.connection;

import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;

public interface SQLConnection {

    void configure(ConfigurationSection section);

    Connection findConnection();

}
