package net.ttmpt;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class ProfanityConfig {
    public static final BuilderCodec<ProfanityConfig> CODEC = BuilderCodec.builder(ProfanityConfig.class, ProfanityConfig::new)
        .append(new KeyedCodec<String>("Action", Codec.STRING),
            (config, string, extraInfo) -> config.action = validateAction(string),
            (config, extraInfo) -> config.action).add()
        .append(new KeyedCodec<String>("FilterResponse", Codec.STRING),
            (config, string, extraInfo) -> config.filterResponse = string,
            (config, extraInfo) -> config.filterResponse).add()
        .append(new KeyedCodec<String[]>("ObscureReplacement", Codec.STRING_ARRAY),
            (config, strings, extraInfo) -> config.obscureReplacement = validateObscureReplacement(strings),
            (config, extraInfo) -> config.obscureReplacement).add()
        .append(new KeyedCodec<String[]>("Whitelist", Codec.STRING_ARRAY),
            (config, strings, extraInfo) -> config.whitelist = strings,
            (config, extraInfo) -> config.whitelist).add()
        .append(new KeyedCodec<String[]>("Blacklist", Codec.STRING_ARRAY),
            (config, strings, extraInfo) -> config.blacklist = strings,
            (config, extraInfo) -> config.blacklist).add()
        .append(new KeyedCodec<String[]>("EnabledBuiltinBlacklists", Codec.STRING_ARRAY),
            (config, strings, extraInfo) -> config.enabledBuiltinBlacklists = strings,
            (config, extraInfo) -> config.enabledBuiltinBlacklists).add()
        .build();

    private String action = "filter";
    private String filterResponse = "Your message was blocked due to offensive language.";
    private String[] obscureReplacement = new String[]{"***"};
    private String[] whitelist = new String[]{};
    private String[] blacklist = new String[]{};
    private String[] enabledBuiltinBlacklists = WordList.BUILTIN_BLACKLISTS.toArray(String[]::new);

    public ProfanityConfig() {}

    protected String getAction() {
        return action;
    }

    protected String getFilterResponse() {
        return filterResponse;
    }

    protected String[] getObscureReplacement() {
        return obscureReplacement;
    }

    protected String[] getWhitelist() {
        return whitelist;
    }

    protected String[] getBlacklist() {
        return blacklist;
    }

    protected String[] getEnabledBuiltinBlacklists() {
        return enabledBuiltinBlacklists;
    }

    private static String validateAction(String string) {
        string = string.toLowerCase();

        if (string.equals("filter") || string.equals("obscure")) {
            return string;
        }

        return "filter";
    }

    private static String[] validateObscureReplacement(String[] strings){
        if (strings.length == 0) {
            return new String[]{"***"};
        }

        return strings;
    }
}
