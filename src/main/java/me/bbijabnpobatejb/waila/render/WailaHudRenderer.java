package me.bbijabnpobatejb.waila.render;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import lombok.val;
import me.bbijabnpobatejb.waila.WailaPlugin;
import me.bbijabnpobatejb.waila.config.WailaConfig;
import me.bbijabnpobatejb.waila.service.RaycastService;
import me.bbijabnpobatejb.waila.ui.WailaHud;
import me.bbijabnpobatejb.waila.util.LangUtil;
import me.bbijabnpobatejb.waila.util.ToolUtil;

import java.util.Objects;

/**
 * Logic for updating the HUD state based on what the player is looking at.
 */
public class WailaHudRenderer {

    /**
     * Called periodically to update the HUD for a specific player.
     */
    public void processHudUpdate(WailaHud hud) {
        val playerRef = hud.getPlayerRef();
        if (!playerRef.isValid()) return;

        val worldUuid = playerRef.getWorldUuid();
        if (worldUuid == null) return;

        val world = Universe.get().getWorld(worldUuid);
        if (world == null) return;

        world.execute(() -> {
            val cfg = WailaPlugin.get().getConfigWrapper().get();
            val target = RaycastService.getTarget(playerRef, world, cfg.getRaycastDistance());

            if (target == null || !cfg.isShow()) {
                hud.setVisible(false);
            } else {
                hud.setVisible(true);
                updateHudOnBlock(hud, target, playerRef, cfg, world);
            }
            // Trigger the UI update
            hud.show();
        });
    }

    private void updateHudOnBlock(WailaHud hud, RaycastService.RaycastResult target, PlayerRef playerRef, WailaConfig cfg, World world) {
        val block = target.block();
        val id = block.getId();

        hud.setBlockName(cfg.isShowBlockName() ? LangUtil.getTranslateKey(block) : "");

        hud.setModName(cfg.isShowModName() ? RaycastService.getModSource(id) : "");


        val toolInfo = cfg.isShowToolEfficiency() ? ToolUtil.getToolInfo(block, world, playerRef) : null;
        hud.setToolEfficiency(Objects.requireNonNullElse(toolInfo, ""));

        hud.setBlockId(cfg.isShowBlockId() ? id : "");
        hud.setItemIcon(cfg.isShowItemIcon() ? id : "");
    }


}