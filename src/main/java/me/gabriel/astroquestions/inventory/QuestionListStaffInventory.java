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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionListStaffInventory extends PagedInventory {

    private final QuestionManager questionManager;

    public QuestionListStaffInventory(QuestionManager questionManager) {
        super("astroquesitons.inventory.staff", "Lista de Dúvidas", InventoryLine.SIX);
        this.questionManager = questionManager;
    }

    @Override
    protected void onCreate(PagedViewer viewer) {
        viewer.setNextPageItemSlot(53);
        viewer.setPreviousPageItemSlot(45);
        setUpdateTime(5);
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

            long createdAt = question.getCreatedAt();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            String[] questionDateSplit = simpleDateFormat.format(new Date(createdAt)).split(" ");

            String questionDate = questionDateSplit[0];
            String questionHour = questionDateSplit[1];

            List<String> lore = new ArrayList<>(
                    InventoryConfiguration.get(InventoryConfiguration::LORE)
            );

            ItemBuilder questionItem = new ItemBuilder(InventoryConfiguration.get(InventoryConfiguration::STAFF_ITEM_MATERIAL))
                    .name(InventoryConfiguration.get(InventoryConfiguration::STAFF_ITEM_NAME)
                            .replace("@player", question.getPlayer())
                            .replace("@id", String.valueOf(question.getId())))
                    .durability(InventoryConfiguration.get(InventoryConfiguration::STAFF_ITEM_DATA))
                    .flag(ItemFlag.values());

            long quotedAt = question.getQuotedAt();

            String[] quoteDateSplit = simpleDateFormat.format(new Date(quotedAt)).split(" ");

            String quoteDate = quoteDateSplit[0];
            String quoteHour = quoteDateSplit[1];

            if (questionManager.isQuoted(question)) {

                lore.addAll(InventoryConfiguration.get(InventoryConfiguration::LORE_REPLIED));

            } else {

                lore.add("");
                lore.add("§aClique aqui para responder.");

            }

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
                    .addDefaultCallback(
                            click -> {

                                click.closeInventory();

                                Player player = click.getPlayer();

                                if (!questionManager.isQuoted(question)) {


                                    String questionOwner = question.getPlayer();
                                    Player questionPlayer = Bukkit.getPlayer(question.getPlayer());

                                    final ChatAsk REPLY_QUESTION = ChatAsk.builder()
                                            .messages(
                                                    "",
                                                    String.format("&aDigite abaixo a resposta para a pergunta de %s.", questionOwner),
                                                    "&aPara cancelar a ação, digite \"&ccancelar&a\"",
                                                    ""
                                            )
                                            .onComplete((asked, message) -> {

                                                if (message.equalsIgnoreCase("cancelar")) {
                                                    asked.sendMessage("§aOperação cancelada com sucesso.");
                                                    return;
                                                }

                                                Question quotedQuestion = questionManager.quote(question, asked.getName(), message, System.currentTimeMillis());

                                                asked.sendMessage(ConfigurationValue.get(ConfigurationValue::REPLY_SENT));

                                                List<String> questionRepliedMessage = ConfigurationValue.get(ConfigurationValue::QUESTION_REPLIED);

                                                for (String replyMessage : questionRepliedMessage) {

                                                    questionPlayer.sendMessage(replyMessage
                                                            .replace("@id", String.valueOf(question.getId()))
                                                            .replace("@reply", quotedQuestion.getQuote())
                                                            .replace("@staff", quotedQuestion.getStaff())
                                                    );

                                                }

                                                this.openInventory(player);

                                            })
                                            .build();

                                    REPLY_QUESTION.addPlayer(player);

                                } else {

                                    player.sendMessage(ConfigurationValue.get(ConfigurationValue::ALREADY_REPLIED));

                                }

                            }
                    );

            items.add(inventoryItem);
        }

        return items;

    }

}
