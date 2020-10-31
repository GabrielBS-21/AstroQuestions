package me.gabriel.astroquestions.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.gabriel.astroquestions.model.Question;
import org.bukkit.entity.Player;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionCreateEvent extends QuestionEvent {

    private final Player player;
    private final Question question;
    private boolean cancelled;

}
