package net.ttmpt;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.util.Config;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import java.util.Random;

public class PlayerChatHandler {
    private Random random = new Random();
    private static final int MAX_WORD_LENGTH = 4;
    private Config<ProfanityConfig> config;

    public PlayerChatHandler(@Nonnull Config<ProfanityConfig> config) {
        this.config = config;
    }

    public void onPlayerChat(@Nonnull PlayerChatEvent event) {
        String message = event.getContent();
        String action = config.get().getAction();

        if (action.equals("filter")) {
            if (isProfane(message)) {
                PlayerRef sender = event.getSender();
                String response = config.get().getFilterResponse();

                if (!response.isEmpty()) {
                    sender.sendMessage(Message.raw(response));
                }

                event.setCancelled(true);
            }
        } else if (action.equals("obscure")) {
            String obscured = checkProfanityAndObscure(message);

            if (obscured != null) {
                event.setContent(obscured);
            }
        }
    }

    private boolean isProfane(@Nonnull String message) {
        message = WordList.normalize(message);
        String[] tokens = message.split(" ");

        for (int i = 0; i < tokens.length; i++) {
            String candidate = tokens[i];

            if (WordList.isBlacklisted(candidate)) {
                return true;
            }

            if (containsFuck(candidate)) {
                return true;
            }

            // Check for profane words without spaces in long words
            if (candidate.length() > 10 && WordList.containsProfanity(candidate)) {
                return true;
            }

            StringBuilder combined = new StringBuilder(candidate);
            for (int j = 1; j < MAX_WORD_LENGTH && i + j < tokens.length; j++) {
                combined.append(tokens[i + j]);
                String combinedCandidate = combined.toString();

                if (WordList.isBlacklisted(combinedCandidate)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Nullable
    private String checkProfanityAndObscure(@Nonnull String originalMessage) {
        String normalized = WordList.normalize(originalMessage);
        String[] tokens = normalized.split(" ");
        String[] originalTokens = originalMessage.split(" ");

        boolean modified = false;

        for (int i = 0; i < tokens.length; i++) {
            if (WordList.isBlacklisted(tokens[i]) || containsFuck(tokens[i])) {
                originalTokens[i] = getObscureReplacement();
                modified = true;
                continue;
            }

            // Check for profane words without spaces in long words
            if (tokens[i].length() > 10 && WordList.containsProfanity(tokens[i])) {
                originalTokens[i] = getObscureReplacement();
                modified = true;
                continue;
            }

            StringBuilder combined = new StringBuilder(tokens[i]);
            for (int j = 1; j < MAX_WORD_LENGTH && i + j < tokens.length; j++) {
                combined.append(tokens[i + j]);

                if (WordList.isBlacklisted(combined.toString())) {
                    for (int k = i; k <= i + j; k++) {
                        originalTokens[k] = getObscureReplacement();
                    }
                    modified = true;
                    i += j; // skip consumed tokens
                    break;
                }
            }
        }

        return modified ? String.join(" ", originalTokens) : null;
    }

    /**
     * Special treatment, because its commonly used and easy to filter.
     */
    private boolean containsFuck(String word) {
        return word.contains("fuck") || word.contains("f*ck") || word.contains("f***");
    }

    private String getObscureReplacement() {
        String[] replacements = config.get().getObscureReplacement();

        int index = random.nextInt(replacements.length);

        return replacements[index];
    }

    public void register(@Nonnull EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(PlayerChatEvent.class, this::onPlayerChat);
    }
}
