package me.gabriel.astroquestions.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.gabriel.astroquestions.AstroQuestions;
import me.gabriel.astroquestions.model.Question;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Singleton
public class QuestionManager {

    @Inject private AstroQuestions plugin;

    public Question createQuestion(Player player, String questionValue) {

        try {

            return CompletableFuture.supplyAsync(() ->
                    plugin.getQuestionDataAccess().insertQuestion(Question.builder()
                        .player(player.getName())
                        .question(questionValue)
                        .createdAt(System.currentTimeMillis())
                        .build())).get();

        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }

    }

    public void quote(Question question){



    }

    public List<Question> questionList(){
        return plugin.getQuestionDataAccess().findAllQuestions();
    }

    public boolean isQuoted(Question question){
        return !question.getQuote().equals("null");
    }

}
