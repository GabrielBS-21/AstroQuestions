package me.gabriel.astroquestions.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TextUtil extends TextComponent {

    public TextUtil(TextComponent textComponent) {
        super(textComponent);
    }

    public TextUtil(BaseComponent... extras) {
        super(extras);
    }

    public TextUtil(String text) {
        super(text);
    }

    public TextUtil text(String text) {
        getExtra().add(new TextComponent(text));
        return this;
    }

    public TextUtil color(ChatColor color) {
        getCurrentComponent().setColor(color);
        return this;
    }

    public TextUtil bold(boolean bool) {
        getCurrentComponent().setBold(bool);
        return this;
    }

    public TextUtil italic(boolean bool) {
        getCurrentComponent().setItalic(bool);
        return this;
    }

    public TextUtil underlined(boolean bool) {
        getCurrentComponent().setUnderlined(bool);
        return this;
    }

    public TextUtil strikethrough(boolean bool) {
        getCurrentComponent().setStrikethrough(bool);
        return this;
    }

    public TextUtil obfuscated(boolean bool) {
        getCurrentComponent().setObfuscated(bool);
        return this;
    }

    public TextUtil insertion(String insertion) {
        getCurrentComponent().setInsertion(insertion);
        return this;
    }

    public TextUtil clickEvent(ClickEvent.Action action, String value) {
        getCurrentComponent().setClickEvent(new ClickEvent(action, value));
        return this;
    }

    public TextUtil hoverEvent(HoverEvent.Action action, String value) {
        getCurrentComponent().setHoverEvent(new HoverEvent(action, new BaseComponent[]{ new TextComponent(value) }));
        return this;
    }

    public TextUtil hoverEvent(HoverEvent.Action action, BaseComponent[] value) {
        getCurrentComponent().setHoverEvent(new HoverEvent(action, value));
        return this;
    }

    public TextUtil extra(BaseComponent component) {
        addExtra(component);
        return this;
    }

    public BaseComponent getCurrentComponent() {
        if (getExtra().size() == 0) return this;
        else return getExtra().get(getExtra().size() - 1);
    }

}
