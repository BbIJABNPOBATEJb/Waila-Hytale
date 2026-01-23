package me.bbijabnpobatejb.waila;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.hypixel.hytale.server.core.util.Config;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import me.bbijabnpobatejb.waila.command.WailaCommand;
import me.bbijabnpobatejb.waila.config.WailaConfig;
import me.bbijabnpobatejb.waila.service.WailaHudService;
import me.bbijabnpobatejb.waila.tick.PlayerTickSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;

import static lombok.AccessLevel.PRIVATE;

/**
 * Main entry point for the Waila plugin.
 */
@Getter
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class WailaPlugin extends JavaPlugin {

    static WailaPlugin instance;
    Config<WailaConfig> configWrapper;

    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    WailaHudService wailaHudService = new WailaHudService();

    public WailaPlugin(JavaPluginInit init) {
        super(init);
        WailaPlugin.instance = this;
        this.configWrapper = withConfig(WailaConfig.CODEC);
    }

    public static WailaPlugin get() {
        return instance;
    }

    @Override
    public void setup() {
        configWrapper.save();
        wailaHudService.setup();
        this.getEntityStoreRegistry().registerSystem(new PlayerTickSystem());


        getCommandRegistry().registerCommand(new WailaCommand());
    }

    @Override
    public void start() {
        val plugin = PluginManager.get().getPlugin(PluginIdentifier.fromString("Buuz135:MultipleHUD"));
        val multipleHUD = plugin != null;
        wailaHudService.start(multipleHUD);
    }

    @Override
    public void shutdown() {
        scheduler.shutdown();
    }

    public static void info(String s) {
        get().getLogger().at(Level.INFO).log(s);
    }

    public void resetConfig() {
        val resolve = getDataDirectory().resolve("config.json");
        try {
            Files.deleteIfExists(resolve);
            configWrapper.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}