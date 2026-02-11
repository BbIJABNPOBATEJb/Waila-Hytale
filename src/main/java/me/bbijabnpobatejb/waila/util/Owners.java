package me.bbijabnpobatejb.waila.util;

import com.hypixel.hytale.assetstore.AssetPack;
import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.server.core.asset.AssetModule;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import lombok.experimental.UtilityClass;
import lombok.val;
import me.bbijabnpobatejb.waila.WailaPlugin;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * This class provides information about which pack/plugin added a given piece of a content.
 */
// From EyeSpy
@UtilityClass
public class Owners {

    private final Map<String, PluginIdentifier> BLOCKS = new HashMap<>();

    /**
     * Reloads the ownership cache, adding support for new entries.
     */
    public void reload() {
        reloadBlocks();
    }


    private void reloadBlocks() {
        val start = System.nanoTime();
        BLOCKS.clear();
        for (val pack : AssetModule.get().getAssetPacks()) {
            val identifier = new PluginIdentifier(pack.getManifest().getGroup(), pack.getManifest().getName());
            val blockTypeKeys = BlockType.getAssetMap().getKeysForPack(pack.getName());
            if (blockTypeKeys == null) continue;
            for (val entry : blockTypeKeys) {
                BLOCKS.put(entry, identifier);
            }
        }
        WailaPlugin.LOGGER.atInfo().log("Determined owners for %d blocks. Took %fms", BLOCKS.size(), (System.nanoTime() - start) / 1_000_000f);
    }


    /**
     * Gets the owner for a block if we know it.
     *
     * @param blockId The block ID to lookup.
     * @return The owner of the block, if it is known.
     */
    @Nullable
    public PluginIdentifier blockOwner(String blockId) {
        return BLOCKS.get(blockId);
    }


}