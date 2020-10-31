package me.gabriel.astroquestions.sql;

import com.google.inject.Singleton;
import me.gabriel.astroquestions.model.Question;
import me.gabriel.astroquestions.sql.provider.DatabaseProvider;
import me.gabriel.astroquestions.sql.provider.document.parser.impl.QuestionDocumentParser;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public final class QuestionDataAccess extends DatabaseProvider {

    public void createTable() {
        update("create table if not exists `astro_questions` (" +
                "`id` int not null auto_increment primary key," +
                "`player` varchar(24) not null, " +
                "`question` text not null, " +
                "`createdAt` double not null," +
                "`staff` varchar(24)," +
                "`quote` text," +
                "`quotedAt` double" +
                ");");
    }

    public List<Question> findAllQuestions() {
        return queryMany("select * from `astro_questions`")
                .stream()
                .map(document -> document.parse(QuestionDocumentParser.getInstance()))
                .collect(Collectors.toList());
    }

    public Question insertQuestion(Question question) {
        update("insert into `astro_questions` (`player`, `question`, `createdAt`) values (?, ?, ?);",
                question.getPlayer(),
                question.getQuestion().trim(),
                question.getCreatedAt());
        return question;
    }

    public void deleteQuestion(Question question) {
        update("delete from `astro_questions` where `player` = ? and `question` = ?",
                question.getPlayer(),
                question.getQuestion());
    }

}
