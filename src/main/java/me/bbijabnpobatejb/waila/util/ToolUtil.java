package me.bbijabnpobatejb.waila.util;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemTool;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import lombok.experimental.UtilityClass;
import lombok.val;

import javax.annotation.Nullable;

@UtilityClass
public class ToolUtil {

    /**
     * Gets the tool power (efficiency) for a specific gathering type.
     *
     * @param tool       ItemTool config.
     * @param gatherType Gathering type (e.g., "mining").
     * @return Power value or 0.0f if not compatible.
     */
    public float getToolEfficiency(ItemTool tool, String gatherType) {
        if (tool == null || tool.getSpecs() == null) {
            return 0.0f;
        }

        for (val spec : tool.getSpecs()) {
            if (spec.getGatherType() != null && spec.getGatherType().equals(gatherType)) {
                return spec.getPower();
            }
        }

        return 0.0f;
    }

    /**
     * Calculates the efficiency of the tool currently held by the player against the target block.
     * Returns a formatted string (localized) if valid, or null.
     */
    public @Nullable Message getToolInfo(BlockType blockType, World world, PlayerRef playerRef) {
        val gathering = blockType.getGathering();
        if (gathering == null) return null;

        val breaking = gathering.getBreaking();
        if (breaking == null) return null;

        val gatherType = breaking.getGatherType();
        if (gatherType == null) return null;

        val store = world.getEntityStore().getStore();
        val reference = playerRef.getReference();
        if (store == null || reference == null) return null;

        val player = store.getComponent(reference, Player.getComponentType());
        if (player == null) return null;

        val inventory = player.getInventory();
        val itemInHand = inventory.getItemInHand();
        if (itemInHand == null) return null;

        val item = itemInHand.getItem();
        val tool = item.getTool();
        val toolEfficiency = getToolEfficiency(tool, gatherType);

        if (toolEfficiency <= 0f) return null;

        val value = String.valueOf(toolEfficiency);
        return LangUtil.formatHudText("mining_speed", "speed", value);
    }
}