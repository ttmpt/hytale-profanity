package net.ttmpt;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class ProfanityConfig {
    public static final BuilderCodec<ProfanityConfig> CODEC = BuilderCodec.builder(ProfanityConfig.class, ProfanityConfig::new)
        .append(new KeyedCodec<String>("Action", Codec.STRING),
            (config, string, extraInfo) -> {
                if (string.equals("filter") || string.equals("obscure")) {
                    config.action = string;
                }
            },
            (config, extraInfo) -> config.action).add()
        .append(new KeyedCodec<String>("FilterResponse", Codec.STRING),
            (config, string, extraInfo) -> config.filterResponse = string,
            (config, extraInfo) -> config.filterResponse).add()
        .append(new KeyedCodec<String[]>("Whitelist", Codec.STRING_ARRAY),
            (config, strings, extraInfo) -> config.whitelist = strings,
            (config, extraInfo) -> config.whitelist).add()
        .append(new KeyedCodec<String[]>("Blacklist", Codec.STRING_ARRAY),
            (config, strings, extraInfo) -> config.blacklist = strings,
            (config, extraInfo) -> config.blacklist).add()
        .append(new KeyedCodec<String[]>("Languages", Codec.STRING_ARRAY),
            (config, strings, extraInfo) -> config.languages = strings,
            (config, extraInfo) -> config.languages).add()
        .build();

    private String action = "filter";
    private String filterResponse = "Your message was blocked due to offensive language.";
    private String[] whitelist = new String[]{};
    private String[] blacklist = new String[]{};
    private String[] languages = new String[]{};

    public ProfanityConfig() {}

    public String getAction() {
        return action;
    }

    public String getFilterResponse() {
        return filterResponse;
    }

    public String[] getWhitelist() {
        return whitelist;
    }

    public String[] getBlacklist() {
        return blacklist;
    }

    public String[] getLanguages() {
        return languages;
    }
}
