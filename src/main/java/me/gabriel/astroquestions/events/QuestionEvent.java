package me.gabriel.astroquestions.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class QuestionEvent extends Event {

    @Getter private static final HandlerList HANDLER_LIST = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
