package me.bbijabnpobatejb.waila.service;

import lombok.Getter;
import lombok.val;
import me.bbijabnpobatejb.waila.WailaPlugin;
import me.bbijabnpobatejb.waila.listeners.WailaHudListener;
import me.bbijabnpobatejb.waila.render.WailaHudRenderer;
import me.bbijabnpobatejb.waila.ui.WailaHud;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Manages the lifecycle of HUD updates and player HUD instances.
 */
@Getter
public class WailaHudService {
    final Map<UUID, WailaHud> playerWailaHud = new ConcurrentHashMap<>();
    final WailaHudListener listener = new WailaHudListener();
    final WailaHudRenderer renderer = new WailaHudRenderer();

    public void setup() {
        listener.register();
    }

    /**
     * Starts the scheduled task that updates the HUD for all connected players.
     */
    public void start() {
        val plugin = WailaPlugin.get();
        val logger = plugin.getLogger();
        val scheduler = plugin.getScheduler();
        val taskRegistry = plugin.getTaskRegistry();

        val saveTask = scheduler.scheduleAtFixedRate(
                () -> {
                    try {
                        for (val hud : playerWailaHud.values()) {
                            renderer.processHudUpdate(hud);
                        }
                    } catch (Exception e) {
                        logger.at(Level.SEVERE).log("Error in Waila scheduler");
                        e.printStackTrace();
                    }
                },
                0,
                50, // 50ms = 20 ticks per second
                TimeUnit.MILLISECONDS
        );

        taskRegistry.registerTask((ScheduledFuture<Void>) saveTask);
    }
}