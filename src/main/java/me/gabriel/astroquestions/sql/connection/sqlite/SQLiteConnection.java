package me.gabriel.astroquestions.sql.connection.sqlite;

import lombok.SneakyThrows;
import me.gabriel.astroquestions.AstroQuestions;
import me.gabriel.astroquestions.sql.connection.SQLConnection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class SQLiteConnection implements SQLConnection {

    private File sqlFile;
    private Connection connection;

    @SneakyThrows
    @Override
    public boolean configure(ConfigurationSection section) {
        AstroQuestions astroQuestions = AstroQuestions.getInstance();
        this.sqlFile = createSQLFile(astroQuestions.getDataFolder(), section.getString("file"));
        return true;
    }

    @Override
    public Connection findConnection() {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.sqlFile);
            } catch (Throwable t) {
                t.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(AstroQuestions.getInstance());
            }
        }

        return this.connection;
    }

    @SneakyThrows
    private File createSQLFile(File dataFolder, String name) {
        File file = new File(dataFolder + File.separator + "sql", name);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            parentFile.mkdirs();

            file.createNewFile();
        }
        return file;
    }

}
