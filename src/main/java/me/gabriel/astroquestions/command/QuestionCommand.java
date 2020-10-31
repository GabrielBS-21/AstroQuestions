package me.gabriel.astroquestions.command;

import com.google.inject.Inject;
import me.gabriel.astroquestions.inventory.QuestionListInventory;
import me.gabriel.astroquestions.manager.QuestionManager;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

public class QuestionCommand {

    @Inject private QuestionManager questionManager;

    @Command(
            name = "duvida",
            aliases = {"question"},
            description = "",
            usage = "",
            permission = "",
            target = CommandTarget.PLAYER
    )
    public void questionCommand(Context<Player> playerContext, String[] questionArgs){

        Player player = playerContext.getSender();

        StringBuilder stringBuilder = new StringBuilder();

        for (String s : questionArgs) {
            stringBuilder.append(s).append(" ");
        }

        String questionValue = stringBuilder.toString();

        questionManager.createQuestion(player, questionValue);

        questionManager.questionList().forEach(question1 -> System.out.println(question1.toString()));

    }

    @Command(
            name = "duvidas",
            aliases = {"questions"},
            description = "",
            usage = "",
            permission = "",
            target = CommandTarget.PLAYER
    )
    public void questionListCommand(Context<Player> playerContext){

        Player player = playerContext.getSender();

        QuestionListInventory questionListInventory = new QuestionListInventory(questionManager);
        questionListInventory.openInventory(player);

    }

}
