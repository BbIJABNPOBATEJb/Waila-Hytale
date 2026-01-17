package me.bbijabnpobatejb.waila.service;

import com.buuz135.mhud.MultipleHUD;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import me.bbijabnpobatejb.waila.WailaPlugin;
import me.bbijabnpobatejb.waila.listeners.WailaHudListener;
import me.bbijabnpobatejb.waila.render.WailaHudRenderer;
import me.bbijabnpobatejb.waila.ui.WailaHud;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the lifecycle of HUD updates and player HUD instances.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WailaHudService {
    Map<PlayerRef, WailaHud> playerWailaHud = new ConcurrentHashMap<>();
    WailaHudListener listener = new WailaHudListener();
    WailaHudRenderer renderer = new WailaHudRenderer();
    @NonFinal
    boolean multipleHUD = false;

    public void setup() {
        listener.register();
    }

    /**
     * Starts the scheduled task that updates the HUD for all connected players.
     */
    public void start(boolean multipleHUD) {
        this.multipleHUD = multipleHUD;
        WailaPlugin.info("Found MultipleHUD plugin. Enable multiple mode");
    }

    public void onTick(Player player, PlayerRef playerRef, int index, ArchetypeChunk<EntityStore> archetypeChunk, CommandBuffer<EntityStore> commandBuffer) {
        val hud = playerWailaHud.computeIfAbsent(playerRef, _ -> {
            val wailaHud = new WailaHud(playerRef);
            if (multipleHUD) {
                MultipleHUD.getInstance().setCustomHud(player, playerRef, "wailaHud", wailaHud);
            } else {
                player.getHudManager().setCustomHud(playerRef, wailaHud);
            }
            return wailaHud;
        });

        renderer.onTick(hud, player, playerRef,index, archetypeChunk,commandBuffer);
    }
}