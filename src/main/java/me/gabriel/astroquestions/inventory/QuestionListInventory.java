package me.gabriel.astroquestions.inventory;

import com.henryfabio.inventoryapi.editor.InventoryEditor;
import com.henryfabio.inventoryapi.enums.InventoryLine;
import com.henryfabio.inventoryapi.inventory.paged.PagedInventory;
import com.henryfabio.inventoryapi.item.InventoryItem;
import com.henryfabio.inventoryapi.viewer.paged.PagedViewer;
import me.gabriel.astroquestions.configuration.ConfigurationValue;
import me.gabriel.astroquestions.inventory.configuration.InventoryConfiguration;
import me.gabriel.astroquestions.manager.QuestionManager;
import me.gabriel.astroquestions.model.Question;
import me.gabriel.astroquestions.util.ChatAsk;
import me.gabriel.astroquestions.util.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionListInventory extends PagedInventory {

    private final QuestionManager questionManager;

    public QuestionListInventory(QuestionManager questionManager) {
        super("astroquesitons.inventory", "Minhas Dúvidas", InventoryLine.SIX);
        this.questionManager = questionManager;
    }

    @Override
    protected void onCreate(PagedViewer viewer) {
        viewer.setNextPageItemSlot(53);
        viewer.setPreviousPageItemSlot(45);
    }

    @Override
    protected void onOpen(PagedViewer viewer, InventoryEditor editor) {

    }

    @Override
    protected void onUpdate(PagedViewer viewer, InventoryEditor editor) {

    }

    @Override
    public List<InventoryItem> getPagesItems(PagedViewer viewer) {

        List<InventoryItem> items = new LinkedList<>();

        for (Question question : questionManager.questionList()) {

            if (!question.getPlayer().equals(viewer.getPlayer().getName())) continue;

            long createdAt = question.getCreatedAt();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            String[] questionDateSplit = simpleDateFormat.format(new Date(createdAt)).split(" ");

            String questionDate = questionDateSplit[0];
            String questionHour = questionDateSplit[1];

            List<String> lore = new ArrayList<>(
                    InventoryConfiguration.get(InventoryConfiguration::LORE)
            );

            ItemBuilder questionItem = new ItemBuilder(InventoryConfiguration.get(InventoryConfiguration::ITEM_MATERIAL))
                    .name(InventoryConfiguration.get(InventoryConfiguration::ITEM_NAME)
                            .replace("@id", String.valueOf(question.getId())))
                    .durability(InventoryConfiguration.get(InventoryConfiguration::ITEM_DATA))
                    .flag(ItemFlag.values());

            long quotedAt = question.getQuotedAt();

            String[] quoteDateSplit = simpleDateFormat.format(new Date(quotedAt)).split(" ");

            String quoteDate = quoteDateSplit[0];
            String quoteHour = quoteDateSplit[1];

            if (questionManager.isQuoted(question)) {

                lore.addAll(InventoryConfiguration.get(InventoryConfiguration::STAFF_LORE_REPLIED));
                lore.add("");

            } else {

                lore.add("");
                lore.add("§aEditar pergunta. §7(Clique esquerdo)");

            }
            lore.add("§aRemover pergunta. §7(Clique direito)");

            String replied = questionManager.isQuoted(question) ? "Respondida." : "Não respondida.";

            List<String> replacedLore = lore
                    .stream()
                    .map(s -> s.replace("@question", question.getQuestion())
                            .replace("@createdAt", questionDate + " às " + questionHour)
                            .replace("@situation", replied)
                            .replace("@staff", question.getStaff())
                            .replace("@repliedAt", quoteDate + " às " + quoteHour)
                            .replace("@reply", question.getQuote()))
                    .collect(Collectors.toList());

            questionItem.lore(replacedLore);

            InventoryItem inventoryItem = new InventoryItem(questionItem.build())
                    .addCallback(
                            ClickType.LEFT,
                            click -> {

                                Player player = click.getPlayer();

                                click.closeInventory();

                                if (!questionManager.isQuoted(question)) {

                                    final ChatAsk EDIT_QUESTION = ChatAsk.builder()
                                            .messages(
                                                    "",
                                                    "&aDigite abaixo qual será o novo texto de sua pergunta.",
                                                    "&aPara cancelar a ação, digite \"&ccancelar&a\"",
                                                    ""
                                            )
                                            .onComplete((asked, message) -> {

                                                if (message.equalsIgnoreCase("cancelar")) {
                                                    asked.sendMessage("§aOperação cancelada com sucesso.");
                                                    return;
                                                }

                                                questionManager.editQuestion(question, message);

                                                asked.sendMessage(ConfigurationValue.get(ConfigurationValue::TEXT_EDITED));

                                                this.openInventory(player);

                                            })
                                            .build();

                                    EDIT_QUESTION.addPlayer(player);

                                } else {

                                    player.sendMessage(ConfigurationValue.get(ConfigurationValue::ALREADY_REPLIED));

                                }

                            }
                    )
                    .addCallback(
                            ClickType.RIGHT,
                            click -> {

                                Player player = click.getPlayer();

                                click.closeInventory();

                                final ChatAsk DELETE_QUESTION = ChatAsk.builder()
                                        .messages(
                                                "",
                                                "&aVocê tem certeza que deseja apagar a sua pergunta?",
                                                "&aDigite \"sim\" para confirmar ou \"&cnão&a\" para cancelar.",
                                                ""
                                        )
                                        .onComplete((asked, message) -> {

                                            if (message.equalsIgnoreCase("não") ||
                                                    message.equalsIgnoreCase("nao") ||
                                                    message.equalsIgnoreCase("cancelar")) {
                                                asked.sendMessage("§aOperação cancelada com sucesso.");
                                                return;
                                            }

                                            questionManager.removeQuestion(question);

                                            asked.sendMessage("§aA sua pergunta foi excluída com sucesso.");

                                            this.openInventory(player);

                                        })
                                        .build();

                                DELETE_QUESTION.addPlayer(player);

                            }
                    );

            items.add(inventoryItem);

        }

        return items;

    }

}
