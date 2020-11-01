package me.gabriel.astroquestions.sql.provider.document.parser.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.gabriel.astroquestions.model.Question;
import me.gabriel.astroquestions.sql.provider.document.Document;
import me.gabriel.astroquestions.sql.provider.document.parser.DocumentParser;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuestionDocumentParser implements DocumentParser<Question> {

    @Getter private static final QuestionDocumentParser instance = new QuestionDocumentParser();

    @Override
    public Question parse(Document document) {
        String staffAsString = document.getString("staff");
        String quote = document.getString("quote");
        Number quotedAt = document.getNumber("quotedAt");
        return new Question(
                document.getNumber("id").intValue(),
                document.getString("player"),
                document.getString("question"),
                document.getNumber("createdAt").longValue(),
                staffAsString != null ? staffAsString : "null",
                quote != null ? quote : "null",
                quotedAt != null ? quotedAt.longValue() : 0
        );
    }

}
