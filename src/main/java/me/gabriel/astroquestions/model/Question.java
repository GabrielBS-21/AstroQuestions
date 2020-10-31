package me.gabriel.astroquestions.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Question {

    private final int id;
    private final String player;
    private final String question;
    private final long createdAt;
    private final String staff;
    private final String quote;
    private final long quotedAt;

}
