package net.ttmpt;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import javax.annotation.Nonnull;
import java.io.FileWriter;
import java.io.IOException;

public class Profanity extends JavaPlugin {
    private final Config<ProfanityConfig> config;

    public Profanity(@Nonnull JavaPluginInit init) {
        super(init);
        this.config = this.withConfig("Profanity", ProfanityConfig.CODEC);
    }

    @Override
    protected void setup() {
        super.setup();
        this.config.save();
        WordList.initialize(config);
        new PlayerChatHandler(config).register(this.getEventRegistry());
    }
}
