package me.bbijabnpobatejb.waila.service;

import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.util.TargetUtil;
import lombok.val;

import javax.annotation.Nullable;

/**
 * Service to handle raycasting logic to detect blocks in front of the player.
 */
public class RaycastService {

    public record RaycastResult(BlockType block) {
    }

    /**
     * Performs a raycast from the player's view.
     * @param playerRef The player reference.
     * @param world The world the player is in.
     * @param maxDistance Maximum distance to check.
     * @return RaycastResult containing the block type, or null if nothing hit.
     */
    @Nullable
    public static RaycastResult getTarget(PlayerRef playerRef, World world, double maxDistance) {
        if (!playerRef.isValid()) return null;

        val reference = playerRef.getReference();
        if (reference == null) return null;

        val store = world.getEntityStore().getStore();
        val hitBlockPos = TargetUtil.getTargetBlock(reference, maxDistance, store);

        if (hitBlockPos != null) {
            short blockId = (short) world.getBlock(hitBlockPos.x, hitBlockPos.y, hitBlockPos.z);
            BlockType blockType = BlockType.getAssetMap().getAsset(blockId);
            return new RaycastResult(blockType);
        }

        return null;
    }

    /**
     * Extracts the mod source from a block ID (e.g., "minecraft:dirt" -> "Minecraft").
     */
    public static String getModSource(String blockId) {
        if (blockId == null || !blockId.contains(":")) return "Hytale";
        String modId = blockId.split(":")[0];
        return Character.toUpperCase(modId.charAt(0)) + modId.substring(1);
    }
}