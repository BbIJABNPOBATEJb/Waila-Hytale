package me.bbijabnpobatejb.waila.render;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
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
    public void onTick(WailaHud hud, Player player, PlayerRef playerRef, int index, ArchetypeChunk<EntityStore> archetypeChunk, CommandBuffer<EntityStore> commandBuffer) {
        val world = player.getWorld();
        if (world == null) return;

        val cfg = WailaPlugin.get().getConfigWrapper().get();
        val target = RaycastService.getTarget(playerRef, world, cfg.getRaycastDistance(),index,archetypeChunk,commandBuffer);

        if (target == null || !cfg.isShow()) {
            hud.setVisible(false);
        } else {
            val empty = target.block().getId().equalsIgnoreCase("empty");
            hud.setVisible(!empty);
            updateHudOnBlock(hud, target, playerRef, cfg, world);
        }
        // Trigger the UI update
        hud.show();
    }

    private void updateHudOnBlock(WailaHud hud, RaycastService.RaycastResult target, PlayerRef playerRef, WailaConfig cfg, World world) {
        val block = target.block();
        val id = block.getId();

        val name = cfg.isShowBlockName();
        hud.setBlockName(name ? LangUtil.getTranslateKey(block) : "");

        hud.setModName(cfg.isShowModName() ? RaycastService.getModSource(id) : "");


        val toolInfo = cfg.isShowToolEfficiency() ? ToolUtil.getToolInfo(block, world, playerRef) : null;
        hud.setToolEfficiency(Objects.requireNonNullElse(toolInfo, Message.empty()));

        hud.setBlockId(cfg.isShowBlockId() ? id : "");
        hud.setItemIcon(cfg.isShowItemIcon() ? id : "");
    }


}