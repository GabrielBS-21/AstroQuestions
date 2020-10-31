package me.gabriel.astroquestions.sql.provider.document.parser;

import me.gabriel.astroquestions.sql.provider.document.Document;

public interface DocumentParser<T> {

    T parse(Document document);

}
