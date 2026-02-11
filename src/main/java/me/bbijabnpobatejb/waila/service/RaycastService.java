package me.bbijabnpobatejb.waila.service;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import lombok.val;
import me.bbijabnpobatejb.waila.WailaPlugin;
import me.bbijabnpobatejb.waila.util.Owners;

import javax.annotation.Nullable;

/**
 * Service to handle raycasting logic to detect blocks in front of the player.
 */
public class RaycastService {

    public record RaycastResult(BlockType block, Ref<ChunkStore> blockRef) {
    }

    /**
     * Performs a raycast from the player's view.
     *
     * @param playerRef      The player reference.
     * @param world          The world the player is in.
     * @param maxDistance    Maximum distance to check.
     * @param index
     * @param archetypeChunk
     * @param commandBuffer
     * @return RaycastResult containing the block type, or null if nothing hit.
     */
    @Nullable
    public static RaycastResult getTarget(PlayerRef playerRef, World world, double maxDistance, int index, ArchetypeChunk<EntityStore> archetypeChunk, CommandBuffer<EntityStore> commandBuffer) {
        if (!playerRef.isValid()) return null;
        val reference = playerRef.getReference();
        if (reference == null) return null;
        val transform = archetypeChunk.getComponent(index, TransformComponent.getComponentType());
        if (transform == null) return null;
        val chunkRef = transform.getChunkRef();
        if (chunkRef == null) return null;

        val worldChunk = chunkRef.getStore().getComponent(chunkRef, WorldChunk.getComponentType());
        if (worldChunk == null) return null;
        val hitBlockPos = TargetUtil.getTargetBlock(reference, maxDistance, commandBuffer);
        if (hitBlockPos == null) {
            return null;
        }

        val blockType = worldChunk.getBlockType(hitBlockPos.x, hitBlockPos.y, hitBlockPos.z);

        val blockRef = worldChunk.getBlockComponentEntity(hitBlockPos.x, hitBlockPos.y, hitBlockPos.z);

        return new RaycastResult(blockType, blockRef);

    }

    public static String getModSource(String blockId) {
        val identifier = Owners.blockOwner(blockId);
        if (identifier == null) {
            return "";
        }
        return identifier.getName();
    }
}