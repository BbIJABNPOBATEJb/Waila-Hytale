package me.bbijabnpobatejb.waila.util;

import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockBreakingDropType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockGathering;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemTool;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemToolSpec;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import lombok.experimental.UtilityClass;
import lombok.val;
import me.bbijabnpobatejb.waila.WailaPlugin;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ToolEfficiencyCache {

    private final Map<String, Map<String, Float>> EFFICIENCY_CACHE = new HashMap<>();

    private final float DEFAULT_HAND_EFFICIENCY = 0f;

    public void init() {
        val assetMap = Item.getAssetMap().getAssetMap();
        Map<String, ItemTool> allTools = new HashMap<>();
        assetMap.forEach((s, item) -> {
            val tool = item.getTool();
            if (tool == null) return;
            allTools.put(s, tool);
        });
        Map<String, BlockGathering> allBlocks = new HashMap<>();
        val blockTypeMap = BlockType.getAssetMap().getAssetMap();
        blockTypeMap.forEach((s, blockType) -> {
            val gathering = blockType.getGathering();
            if (gathering == null) return;
            allBlocks.put(s, gathering);
        });
        buildCache(allTools, allBlocks);
    }


    void buildCache(Map<String, ItemTool> allTools, Map<String, BlockGathering> allBlocks) {
        EFFICIENCY_CACHE.clear();

        Map<String, Map<String, Float>> toolsByGatherType = new HashMap<>();

        for (Map.Entry<String, ItemTool> entry : allTools.entrySet()) {
            String itemId = entry.getKey();
            ItemTool tool = entry.getValue();

            if (tool.getSpecs() != null) {
                for (ItemToolSpec spec : tool.getSpecs()) {
                    String type = spec.getGatherType();
                    if (type != null) {
                        toolsByGatherType
                                .computeIfAbsent(type, k -> new HashMap<>())
                                .put(itemId, spec.getPower());
                    }
                }
            }
        }

        for (Map.Entry<String, BlockGathering> entry : allBlocks.entrySet()) {
            String blockId = entry.getKey();
            BlockGathering gathering = entry.getValue();

            BlockBreakingDropType breaking = gathering.getBreaking();

            if (breaking != null && breaking.getGatherType() != null) {
                String requiredGather = breaking.getGatherType();

                Map<String, Float> effectiveTools = toolsByGatherType.get(requiredGather);

                if (effectiveTools != null) {
                    EFFICIENCY_CACHE.put(blockId, new HashMap<>(effectiveTools));
                }
            }
        }

        WailaPlugin.info("ToolEfficiencyCache initialized. Mapped blocks: " + EFFICIENCY_CACHE.size());
    }

    public float getEfficiency(String blockId, String itemId) {
        if (blockId == null || itemId == null) {
            return DEFAULT_HAND_EFFICIENCY;
        }

        Map<String, Float> blockTools = EFFICIENCY_CACHE.get(blockId);

        if (blockTools == null) {
            return DEFAULT_HAND_EFFICIENCY;
        }

        Float power = blockTools.get(itemId);

        if (power != null) {
            return power;
        }

        return DEFAULT_HAND_EFFICIENCY;
    }

    public @Nullable Map<String, Float> getEfficiencyTools(String blockId) {
        if (blockId == null) {
            return new Object2FloatOpenHashMap<>();
        }
        return EFFICIENCY_CACHE.get(blockId);
    }

    public @Nullable String getMostEfficiencyTool(String blockId) {
        val map = getEfficiencyTools(blockId);
        if (map == null) {
            return null;
        }
        return getBestToolType(map, .1f);
    }

    public static String getBestToolType( Map<String, Float> tools, float min) {
        String bestKey = null;
        float maxEfficiency = -1.0f;

        for (val entry : tools.entrySet()) {
            float value = entry.getValue();
            String key = entry.getKey();

            if (value >= min && value > maxEfficiency) {
                maxEfficiency = value;
                bestKey = key;
            }
        }

        if (bestKey != null) {
            return extractToolType(bestKey);
        }

        return null;
    }

    private static String extractToolType(String key) {
        if (key.startsWith("Tool_")) {
            val parts = key.split("_");
            if (parts.length >= 2) {
                return parts[1];
            }
        }
        return key;
    }

}