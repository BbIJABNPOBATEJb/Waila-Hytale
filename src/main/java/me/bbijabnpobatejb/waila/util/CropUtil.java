package me.bbijabnpobatejb.waila.util;

import com.hypixel.hytale.builtin.adventure.farming.states.FarmingBlock;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockGathering;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.HarvestingDropType;

import com.hypixel.hytale.server.core.asset.type.blocktype.config.farming.FarmingData;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.farming.FarmingStageData;
import com.hypixel.hytale.server.core.modules.interaction.BlockHarvestUtils;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

import lombok.experimental.UtilityClass;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;


import javax.annotation.Nullable;

@UtilityClass
public class CropUtil {

    /**
     * Gets crop information for the given block type and its reference in the world.
     */
    public @Nullable String getCropInfo(BlockType blockType, Ref<ChunkStore> blockRef, World world, PlayerRef playerRef) {
        Store<ChunkStore> chunkStoreWorld = world.getChunkStore().getStore();
        if (blockRef == null) { return basicBlockInfo(blockType); }

        FarmingBlock farmingComponent = chunkStoreWorld.getComponent(blockRef, FarmingBlock.getComponentType());
        FarmingData farmingData = blockType.getFarming();

        if (farmingComponent == null || farmingData == null) {
            return basicBlockInfo(blockType);
        }

        float growthProgress = farmingComponent.getGrowthProgress();
        int generation = farmingComponent.getGeneration();
        String currentStageSet = farmingComponent.getCurrentStageSet();

        if (farmingData.getStages() == null) {
            return "Stage: %d".formatted(generation);
        }

        FarmingStageData[] stages = farmingData.getStages().get(currentStageSet);
        float growthPercent = growthProgress / stages.length * 100.0f;

        return "Growth: %.2f%% (Stage %d)".formatted(growthPercent, generation);
    }

    public @Nullable String getCropItemId(BlockType blockType) {
        HarvestingDropType harvest = blockType.getGathering().getHarvest();
        if (harvest == null) {
            return blockType.getId();
        }
        String itemId = harvest.getItemId();
        String dropListId = harvest.getDropListId();
        return BlockHarvestUtils.getDrops(blockType, 1, itemId, dropListId).getFirst().getItemId();
    }

    private String basicBlockInfo(BlockType blockType) {
        BlockGathering gathering = blockType.getGathering();
        FarmingData farming = blockType.getFarming();
        if (farming != null && gathering.isHarvestable()) {
            return "Harvestable";
        }
        return "";
    }
}