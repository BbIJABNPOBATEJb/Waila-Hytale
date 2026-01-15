package me.bbijabnpobatejb.waila;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.bbijabnpobatejb.waila.command.WailaCommand;
import me.bbijabnpobatejb.waila.config.WailaConfig;
import me.bbijabnpobatejb.waila.service.WailaHudService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;

/**
 * Main entry point for the Waila plugin.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WailaPlugin extends JavaPlugin {

    static WailaPlugin instance;
    final Config<WailaConfig> configWrapper;

    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    final WailaHudService wailaHudService = new WailaHudService();

    public WailaPlugin(JavaPluginInit init) {
        super(init);
        instance = this;
        this.configWrapper = withConfig(WailaConfig.CODEC);
    }

    public static WailaPlugin get() {
        return instance;
    }

    @Override
    public void setup() {
        configWrapper.save();
        wailaHudService.setup();
        getCommandRegistry().registerCommand(new WailaCommand());
    }

    @Override
    public void start() {
        wailaHudService.start();
    }

    @Override
    public void shutdown() {
        scheduler.shutdown();
    }

    public static void info(String s) {
        get().getLogger().at(Level.INFO).log(s);
    }


}