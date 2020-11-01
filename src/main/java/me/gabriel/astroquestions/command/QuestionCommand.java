package me.gabriel.astroquestions.command;

import com.google.inject.Inject;
import me.gabriel.astroquestions.configuration.ConfigurationValue;
import me.gabriel.astroquestions.inventory.QuestionListInventory;
import me.gabriel.astroquestions.inventory.QuestionListStaffInventory;
import me.gabriel.astroquestions.manager.QuestionManager;
import me.gabriel.astroquestions.util.TextUtil;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class QuestionCommand {

    @Inject
    private QuestionManager questionManager;

    @Command(
            name = "duvida",
            aliases = {"question"},
            description = "Escreva uma dúvida para os membros da equipe.",
            target = CommandTarget.PLAYER
    )
    public void questionCommand(Context<Player> playerContext, String[] questionArgs) {

        Player player = playerContext.getSender();

        StringBuilder stringBuilder = new StringBuilder();

        for (String s : questionArgs) {
            stringBuilder.append(s).append(" ");
        }

        String questionValue = stringBuilder.toString();

        questionManager.createQuestion(player, questionValue);

        player.sendMessage(ConfigurationValue.get(ConfigurationValue::QUESTION_SENT));

        List<String> newQuestionMessage = ConfigurationValue.get(ConfigurationValue::NEW_QUESTION);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

            if (onlinePlayer.hasPermission("astroquestions.staff")) {

                for (String message : newQuestionMessage) {

                    TextUtil jsonMessage = new TextUtil(message);
                    jsonMessage.clickEvent(ClickEvent.Action.RUN_COMMAND, "/duvidas");

                    onlinePlayer.spigot().sendMessage(jsonMessage);

                }

            }

        }

    }

    @Command(name = "duvida.listar")
    public void questionListCommand(Context<Player> playerContext) {

        Player player = playerContext.getSender();

        QuestionListInventory questionListInventory = new QuestionListInventory(questionManager);
        questionListInventory.openInventory(player);

    }

    @Command(
            name = "duvidas",
            aliases = {"questions"},
            description = "Veja a lista de perguntas.",
            permission = "astroquestions.staff",
            target = CommandTarget.PLAYER
    )
    public void questionStaffListCommand(Context<Player> playerContext) {

        Player player = playerContext.getSender();

        QuestionListStaffInventory questionListStaffInventory = new QuestionListStaffInventory(questionManager);
        questionListStaffInventory.openInventory(player);


    }

}
