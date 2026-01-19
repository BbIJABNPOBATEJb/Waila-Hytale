package me.bbijabnpobatejb.waila.util;

import com.hypixel.hytale.builtin.adventure.farming.states.FarmingBlock;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.interaction.BlockHarvestUtils;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import lombok.experimental.UtilityClass;
import lombok.val;

import javax.annotation.Nullable;

@UtilityClass
public class CropUtil {

    /**
     * Gets crop information for the given block type and its reference in the world.
     */
    public Message getCropInfo(BlockType blockType, Ref<ChunkStore> blockRef, World world, PlayerRef playerRef) {
        val chunkStoreWorld = world.getChunkStore().getStore();
        if (blockRef == null) {
            return basicBlockInfo(blockType);
        }

        val farmingComponent = chunkStoreWorld.getComponent(blockRef, FarmingBlock.getComponentType());
        val farmingData = blockType.getFarming();

        if (farmingComponent == null || farmingData == null) {
            return basicBlockInfo(blockType);
        }

        val growthProgress = farmingComponent.getGrowthProgress();
        val generation = farmingComponent.getGeneration();
        val currentStageSet = farmingComponent.getCurrentStageSet();

        val valueOf = String.valueOf(generation);
        val stageMessage = LangUtil.formatHudText("crop_stage", "generation", valueOf);
        if (farmingData.getStages() == null) {
            return stageMessage;
        }

        val stages = farmingData.getStages().get(currentStageSet);
        val max = Math.max(stages.length, 1);
        val percent = growthProgress / max * 100.0f;

        val fromFloat = StringUtil.fromFloat(percent, 2);
        return LangUtil.formatHudText("crop_growth_stage",
                "percent", fromFloat,
                "generation", valueOf);
    }

    public @Nullable String getCropItemId(BlockType blockType) {
        val harvest = blockType.getGathering().getHarvest();
        if (harvest == null) {
            return blockType.getId();
        }
        val itemId = harvest.getItemId();
        val dropListId = harvest.getDropListId();
        return BlockHarvestUtils.getDrops(blockType, 1, itemId, dropListId).getFirst().getItemId();
    }

    private Message basicBlockInfo(BlockType blockType) {
        val gathering = blockType.getGathering();
        val farming = blockType.getFarming();
        if (farming != null && gathering.isHarvestable()) {
            return Message.translation("waila.hud.waila.harvestable");
        }
        return Message.empty();
    }
}