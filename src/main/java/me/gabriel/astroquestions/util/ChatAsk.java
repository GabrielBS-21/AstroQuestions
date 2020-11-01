package me.gabriel.astroquestions.util;

import me.gabriel.astroquestions.AstroQuestions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;

public class ChatAsk {

    private final static WeakHashMap<Player, ChatAsk> questioneds = new WeakHashMap<>();

    static {
        AstroQuestions plugin = AstroQuestions.getInstance();
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onChat(AsyncPlayerChatEvent event) {
                ChatAsk questioned = questioneds.get(event.getPlayer());
                if (questioned != null) {
                    event.setCancelled(true);
                    BiConsumer<Player, String> consumer = questioned.getOnComplete();
                    String message = event.getMessage();
                    Player player = event.getPlayer();
                    questioneds.remove(player);
                    consumer.accept(player, message);
                }
            }

            @EventHandler
            public void onQuit(PlayerQuitEvent e) {
                questioneds.remove(e.getPlayer());
            }
        }, plugin);
    }

    private final String[] messages;
    private final BiConsumer<Player, String> onComplete;

    private ChatAsk(Builder builder) {
        messages = builder.messages;
        onComplete = builder.onComplete;
    }

    public void addPlayer(Player player) {
        questioneds.put(player, this);

        for (String message : messages) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public BiConsumer<Player, String> getOnComplete() {
        return onComplete;
    }

    public String[] getMessages() {
        return messages;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private String[] messages;
        private BiConsumer<Player, String> onComplete;

        private Builder() {
        }

        public Builder messages(String... messages) {
            this.messages = messages;
            return this;
        }

        public Builder onComplete(BiConsumer<Player, String> onComplete) {
            this.onComplete = onComplete;
            return this;
        }

        public ChatAsk build() {
            return new ChatAsk(this);
        }
    }

}
