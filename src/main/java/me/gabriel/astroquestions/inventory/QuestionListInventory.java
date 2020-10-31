package me.gabriel.astroquestions.inventory;

import com.henryfabio.inventoryapi.editor.InventoryEditor;
import com.henryfabio.inventoryapi.enums.InventoryLine;
import com.henryfabio.inventoryapi.inventory.paged.PagedInventory;
import com.henryfabio.inventoryapi.item.InventoryItem;
import com.henryfabio.inventoryapi.viewer.paged.PagedViewer;
import me.gabriel.astroquestions.manager.QuestionManager;
import me.gabriel.astroquestions.model.Question;
import me.gabriel.astroquestions.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

import java.text.SimpleDateFormat;
import java.util.*;

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

            if(!question.getPlayer().equals(viewer.getPlayer().getName())) continue;

            long createdAt = question.getCreatedAt();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            String[] questionDateSplit = simpleDateFormat.format(new Date(createdAt)).split(" ");

            String questionDate = questionDateSplit[0];
            String questionHour = questionDateSplit[1];

            List<String> lore = new ArrayList<>(Arrays.asList(
                    "",
                    "§7ID: §f#" + question.getId(),
                    "§7Dúvida: §f" + question.getQuestion(),
                    "§7Criado em: §f" + questionDate + " às " + questionHour,
                    "§7Status: §f" + (questionManager.isQuoted(question) ? "Respondida" : "Não respondida."))
            );

            ItemBuilder questionItem = new ItemBuilder(Material.PAPER)
                    .name("§aDúvida de " + question.getPlayer())
                    .flag(ItemFlag.values());


            long quotedAt = question.getQuotedAt();

            String[] quoteDateSplit = simpleDateFormat.format(new Date(quotedAt)).split(" ");

            String quoteDate = quoteDateSplit[0];
            String quoteHour = quoteDateSplit[1];

            if (questionManager.isQuoted(question)) {

                lore.add("  §7Respondida por: §f" + question.getStaff());
                lore.add("  §7Respondida em: §f" + quoteDate + " às " + quoteHour);
                lore.add("  §7Resposta: §f" + question.getQuote());

            }else{

                lore.add("");
                lore.add("§aClique aqui para editar a pergunta.");

            }

            questionItem.lore(lore);

            InventoryItem inventoryItem = new InventoryItem(questionItem.build())
                    .addDefaultCallback(click -> {

                    });

            items.add(inventoryItem);

        }

        return items;

    }

}
