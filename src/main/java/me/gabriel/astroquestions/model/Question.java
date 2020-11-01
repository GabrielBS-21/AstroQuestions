package me.gabriel.astroquestions.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class Question {

    private int id;
    private String player;
    private String question;
    private long createdAt;
    private String staff;
    private String quote;
    private long quotedAt;

}
