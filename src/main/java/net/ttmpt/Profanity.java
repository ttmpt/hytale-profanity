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
        logToFile("init");
        Config<ProfanityConfig> config;
        try {
            config = this.withConfig("Profanity", ProfanityConfig.CODEC);
        } catch (Exception e) {
            logToFile(e.toString());
            config = this.withConfig("Profanity", ProfanityConfig.CODEC);
        }
        this.config = config;
        logToFile("config loaded");
    }

    @Override
    protected void setup() {
        super.setup();
        this.config.save();
        logToFile("config saved");
        WordList.initialize(config);
        logToFile("words loaded");
        try {
            new PlayerChatHandler(config).register(this.getEventRegistry());
        } catch (Exception e) {
            logToFile(e.toString());
        }
        logToFile("handler init");
    }

    public static void logToFile(String message) {
        try (FileWriter fw = new FileWriter("/tmp/profanity.log", true)) {
            fw.write(message + "\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
