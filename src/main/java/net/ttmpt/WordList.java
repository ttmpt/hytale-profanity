package net.ttmpt;

import com.hypixel.hytale.server.core.util.Config;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import javax.annotation.Nonnull;

public class WordList {
    private static Set<String> blacklist = new HashSet<>();
    private static Set<String> blacklistLong = new HashSet<>();
    private static boolean initialized = false;
    private static boolean isFuckBlacklisted = false;

    protected static final Set<String> BUILTIN_BLACKLISTS = Set.of(
        "cs", "da", "de", "en", "es", "fa", "fi", "fil", "fr", "hi",
        "hu", "it", "nl", "no", "pl", "pt", "ru", "sv", "tr"
    );

    protected static void initialize(@Nonnull Config<ProfanityConfig> config) {
        if (initialized) return;
        initialized = true;

        Set<String> requestedBlacklists = new HashSet<>(Arrays.asList(config.get().getEnabledBuiltinBlacklists()));

        for (String lang_code : requestedBlacklists) {
            if (BUILTIN_BLACKLISTS.contains(lang_code)) {
                loadBuiltinBlacklist(lang_code);
            }
        }

        for (String phrase : config.get().getBlacklist()) {
            blacklist.add(normalize(phrase).replaceAll(" ", ""));
        }

        for (String phrase : config.get().getWhitelist()) {
            blacklist.remove(normalize(phrase).replaceAll(" ", ""));
        }

        // Precalculate words with length >= 5
        for (String word : blacklist) {
            if (word.length() >= 5) {
                blacklistLong.add(word);
            }
        }

        if (blacklist.contains("fuck")) {
            isFuckBlacklisted = true;
        }
    }

    protected static boolean getIsFuckBlacklisted() {
        return isFuckBlacklisted;
    }

    private static void loadBuiltinBlacklist(@Nonnull String lang_code) {
        InputStream is = WordList.class.getClassLoader().getResourceAsStream("profanity/" + lang_code);
        if (is == null) {
            initialized = true;
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String normalized = normalize(line).replaceAll(" ", "");
                blacklist.add(normalized);
                if (normalized.length() >= 5) {
                    blacklistLong.add(normalized);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading profanity list: " + e.getMessage());
        }
    }

    protected static String normalize(@Nonnull String message) {
        message = message.toLowerCase();
        message = message.replace('$', 's')
                         .replace('§', 's')
                         .replace('€', 'e')
                         .replace('@', 'a')
                         .replace('0', 'o')
                         .replace('1', 'i')
                         .replace('2', 's')
                         .replace('3', 'e')
                         .replace('5', 's')
                         .replace('8', 'b');

        // replace unwanted ASCII symbols with space, but keep letters and numbers
        message = message.replaceAll("[\\x00-\\x7F&&[^\\p{L}0-9]]", " ");
        message = message.replaceAll(" +", " ").trim();

        return message;
    }

    protected static boolean isBlacklisted(@Nonnull String phrase) {
        return blacklist.contains(phrase);
    }

    protected static boolean containsProfanity(@Nonnull String word) {
        for (String profanity : blacklistLong) {
            if (word.contains(profanity)) {
                return true;
            }
        }
        return false;
    }
}
