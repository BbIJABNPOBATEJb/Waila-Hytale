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
        val dop = multipleHUD ? " Enable multiple mode" : "";
        WailaPlugin.info("Found MultipleHUD plugin." + dop);
    }


    public void onTick(Player player, PlayerRef playerRef, int index, ArchetypeChunk<EntityStore> archetypeChunk, CommandBuffer<EntityStore> commandBuffer) {

        val wailaHud = playerWailaHud.computeIfAbsent(playerRef, WailaHud::new);
        val canShow = (player.getWindowManager().getWindows().isEmpty() && player.getPageManager().getCustomPage() == null);
        if (!canShow) return;

        renderer.onTick(wailaHud, player, playerRef, index, archetypeChunk, commandBuffer,multipleHUD);

        if (multipleHUD) {
            MultipleHUD.getInstance().setCustomHud(player, playerRef, "WailaMain", wailaHud);
        } else {
            player.getHudManager().setCustomHud(playerRef, wailaHud);
        }
    }

    public void updatePreview(PlayerRef playerRef, boolean preview, boolean mirrorX, float guiScale, int guiOffsetX, int guiOffsetY) {
        val wailaHud = playerWailaHud.get(playerRef);
        if (wailaHud == null) return;
        wailaHud.setPreview(preview);
        wailaHud.setMirrorX(mirrorX);
        wailaHud.setPreviewGuiScale(guiScale);
        wailaHud.setPreviewGuiOffsetX(guiOffsetX);
        wailaHud.setPreviewGuiOffsetY(guiOffsetY);
    }
}