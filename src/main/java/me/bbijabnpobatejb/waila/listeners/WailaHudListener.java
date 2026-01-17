package me.bbijabnpobatejb.waila.listeners;

import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import lombok.val;
import me.bbijabnpobatejb.waila.WailaPlugin;
import me.bbijabnpobatejb.waila.ui.WailaHud;

/**
 * Listener to handle player connection and disconnection.
 * Assigns a specific HUD instance to each player.
 */
public class WailaHudListener {

    public void register() {
        val registry = WailaPlugin.get().getEventRegistry();
        registry.register(PlayerDisconnectEvent.class, this::onQuit);
    }



    void onQuit(PlayerDisconnectEvent event) {
        val huds = WailaPlugin.get().getWailaHudService().getPlayerWailaHud();
        huds.remove(event.getPlayerRef());
    }
}