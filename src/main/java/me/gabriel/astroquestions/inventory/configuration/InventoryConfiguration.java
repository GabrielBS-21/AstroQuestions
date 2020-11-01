package me.gabriel.astroquestions.inventory.configuration;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.gabriel.astroquestions.AstroQuestions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.stream.Collectors;

@Accessors(fluent = true)
@Getter
public class InventoryConfiguration {

    private static final InventoryConfiguration instance = new InventoryConfiguration();

    private final FileConfiguration configuration = AstroQuestions.getInstance().getConfig();

    // DEFAULT

    private final String INVENTORY_TITLE = translateMessage("question.inventory.title");
    private final int INVENTORY_LINES = configuration.getInt("question.inventory.lines");

    private final String ITEM_NAME = translateColor("question.inventory.item.name");
    private final Material ITEM_MATERIAL = Material.valueOf(configuration.getString("question.inventory.item.material"));

    private final List<String> LORE = translateMessageList("question.inventory.item.lore");
    private final List<String> LORE_REPLIED = translateMessageList("question.inventory.item.lore-replied");

    // STAFF

    private final String STAFF_INVENTORY_TITLE = translateMessage("question.staff-inventory.title");
    private final int STAFF_INVENTORY_LINES = configuration.getInt("question.staff-inventory.lines");

    private final String STAFF_ITEM_NAME = translateColor("question.staff-inventory.item.name");
    private final Material STAFF_ITEM_MATERIAL = Material.valueOf(configuration.getString("question.staff-inventory.item.material"));

    private final List<String> STAFF_LORE = translateMessageList("question.staff-inventory.item.lore");
    private final List<String> STAFF_LORE_REPLIED = translateMessageList("question.staff-inventory.item.lore-replied");

    public static <T> T get(InventoryConfiguration.InventorySupplier<T> supplier) {
        return supplier.get(InventoryConfiguration.instance);
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
    public interface InventorySupplier<T> {
        T get(InventoryConfiguration inventoryConfiguration);
    }

}
