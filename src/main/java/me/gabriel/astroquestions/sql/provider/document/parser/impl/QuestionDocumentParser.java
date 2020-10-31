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
        return Question.builder()
                .id(document.getNumber("id").intValue())
                .player(document.getString("player"))
                .question(document.getString("question"))
                .createdAt(document.getNumber("createdAt").longValue())
                .staff(staffAsString != null ? staffAsString : "null")
                .quote(quote != null ? quote : "null")
                .quotedAt(quotedAt != null ? quotedAt.longValue() : 0)
                .build();
    }

}
