package me.gabriel.astroquestions.configuration;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.gabriel.astroquestions.AstroQuestions;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.stream.Collectors;

@Accessors(fluent = true)
@Getter
public class ConfigurationValue {

    private static final ConfigurationValue instance = new ConfigurationValue();

    private final FileConfiguration configuration = AstroQuestions.getInstance().getConfig();

    private final String ERROR_MESSAGE = translateMessage("messages.error");
    private final String INVALID_TARGET_MESSAGE = translateMessage("messages.invalid-target");
    private final String INVALID_USAGE_MESSAGE = translateMessage("messages.invalid-usage");
    private final String NO_PERMISSION_MESSAGE = translateMessage("messages.no-permission");
    private final String ALREADY_REPLIED = translateMessage("messages.already-replied");
    private final String REPLY_SENT = translateMessage("messages.reply-sent");
    private final String TEXT_EDITED = translateMessage("messages.text-edited");

    private final List<String> NEW_QUESTION = translateMessageList("question.new-question");
    private final List<String> QUESTION_REPLIED = translateMessageList("question.question-replied");

    public static <T> T get(ConfigurationSupplier<T> supplier) {
        return supplier.get(ConfigurationValue.instance);
    }

    public String translateColor(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String translateMessage(String key) {
        return translateColor(configuration.getString(key));
    }

    public List<String> translateMessageList(String key) {
        return configuration.getStringList(key)
                .stream()
                .map(this::translateColor)
                .collect(Collectors.toList());
    }

    @FunctionalInterface
    public interface ConfigurationSupplier<T> {
        T get(ConfigurationValue configuration);
    }

}
