package me.gabriel.astroquestions.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.gabriel.astroquestions.AstroQuestions;
import me.gabriel.astroquestions.model.Question;
import org.bukkit.entity.Player;

import java.util.List;

@Singleton
public class QuestionManager {

    @Inject private AstroQuestions plugin;

    public void createQuestion(Player player, String questionValue) {

        try {
            plugin.getQuestionDataAccess().insertQuestion(Question.builder()
                    .player(player.getName())
                    .question(questionValue)
                    .createdAt(System.currentTimeMillis())
                    .build());

        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public Question quote(Question question, String staff, String quote, long quotedAt) {
        plugin.getQuestionDataAccess().updateQuestion(question, staff, quote, quotedAt);
        question.setStaff(staff);
        question.setQuote(quote);
        question.setQuotedAt(quotedAt);
        return question;
    }

    public void editQuestion(Question question, String newQuestionValue) {

        plugin.getQuestionDataAccess().update("UPDATE `astro_questions` SET `question` = ? WHERE ID = ?",
                newQuestionValue,
                question.getId());

    }

    public void removeQuestion(Question question) {
        plugin.getQuestionDataAccess().deleteQuestion(question);
    }

    public List<Question> questionList() {
        return plugin.getQuestionDataAccess().findAllQuestions();
    }

    public boolean isQuoted(Question question) {
        return !question.getQuote().equals("null");
    }

}
