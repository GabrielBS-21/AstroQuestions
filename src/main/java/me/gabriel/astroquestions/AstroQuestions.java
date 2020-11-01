package me.gabriel.astroquestions;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.henryfabio.inventoryapi.manager.InventoryManager;
import lombok.Getter;
import me.bristermitten.pdm.PDMBuilder;
import me.bristermitten.pdm.PluginDependencyManager;
import me.gabriel.astroquestions.command.QuestionCommand;
import me.gabriel.astroquestions.configuration.ConfigurationValue;
import me.gabriel.astroquestions.sql.QuestionDataAccess;
import me.gabriel.astroquestions.sql.connection.SQLConnection;
import me.gabriel.astroquestions.sql.connection.mysql.MySQLConnection;
import me.gabriel.astroquestions.sql.connection.sqlite.SQLiteConnection;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Getter
public final class AstroQuestions extends JavaPlugin {

    private CompletableFuture<Void> dependencyLoader;

    private Injector injector;

    private SQLConnection sqlConnection;

    @Inject private QuestionDataAccess questionDataAccess;

    public static AstroQuestions getInstance() {
        return getPlugin(AstroQuestions.class);
    }

    @Override
    public void onLoad() {

        saveDefaultConfig();

        PluginDependencyManager dependencyManager = new PDMBuilder(this).build();
        dependencyLoader = dependencyManager.loadAllDependencies();
        dependencyLoader.thenRun(() -> {

            configureSQLConnection();

            try {
                getLogger().info("Carregando informações...");

                AstroQuestions instance = this;
                this.injector = Guice.createInjector(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(AstroQuestions.class).toInstance(instance);
                        bind(Logger.class).annotatedWith(Names.named("main")).toInstance(instance.getLogger());
                        bind(SQLConnection.class).toInstance(instance.sqlConnection);
                    }
                });
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });

    }

    @Override
    public void onEnable() {

        dependencyLoader.thenRun(() -> {

            try {

                this.injector.injectMembers(this);

                InventoryManager.enable(this);

                this.questionDataAccess.createTable();

                BukkitFrame bukkitFrame = new BukkitFrame(this);

                QuestionCommand questionCommand = new QuestionCommand();

                this.injector.injectMembers(questionCommand);
                bukkitFrame.registerCommands(questionCommand);

                MessageHolder messageHolder = bukkitFrame.getMessageHolder();
                messageHolder.setMessage(MessageType.ERROR, ConfigurationValue.get(ConfigurationValue::ERROR_MESSAGE));
                messageHolder.setMessage(MessageType.INCORRECT_TARGET, ConfigurationValue.get(ConfigurationValue::INVALID_TARGET_MESSAGE));
                messageHolder.setMessage(MessageType.INCORRECT_USAGE, ConfigurationValue.get(ConfigurationValue::INVALID_USAGE_MESSAGE));
                messageHolder.setMessage(MessageType.NO_PERMISSION, ConfigurationValue.get(ConfigurationValue::NO_PERMISSION_MESSAGE));

                getLogger().info("Plugin carregado com sucesso.");

            } catch (Throwable t) {
                t.printStackTrace();
            }

        });

    }

    @Override
    public void onDisable() {

        getLogger().info("Informações salvas.");
        getLogger().info("Plugin descarregado com sucesso.");

        try{
            this.sqlConnection.findConnection().close();
        } catch (SQLException e){
            e.printStackTrace();
            getLogger().warning("Não foi possível fechar a conexão com o banco de dados.");
        }

    }

    private void configureSQLConnection() {
        ConfigurationSection connectionSection = getConfig().getConfigurationSection("connection");
        this.sqlConnection = new MySQLConnection();
        if (!sqlConnection.configure(connectionSection.getConfigurationSection("mysql"))) {
            this.sqlConnection = new SQLiteConnection();
            this.sqlConnection.configure(connectionSection.getConfigurationSection("sqlite"));
        }
    }

}
