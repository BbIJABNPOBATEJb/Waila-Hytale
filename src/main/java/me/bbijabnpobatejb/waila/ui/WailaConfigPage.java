package me.bbijabnpobatejb.waila.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import me.bbijabnpobatejb.waila.config.WailaConfig;

import javax.annotation.Nonnull;

/**
 * Interactive UI page for changing plugin configuration.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WailaConfigPage extends InteractiveCustomUIPage<WailaConfigPage.ConfigData> {

    public static final String BUTTON = "Button";
    public static final String SAVE_BUTTON = "Save" + BUTTON;
    public static final String CLOSE_BUTTON = "Close" + BUTTON;

    Config<WailaConfig> configWrapper;

    public WailaConfigPage(PlayerRef playerRef, Config<WailaConfig> configWrapper) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, ConfigData.CODEC);
        this.configWrapper = configWrapper;
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder builder, @Nonnull UIEventBuilder events, @Nonnull Store<EntityStore> store) {
        builder.append("Pages/waila_config.ui");

        val config = configWrapper.get();
        builder.set("#ShowCheck.Value", config.isShow());
        builder.set("#ShowBlockNameCheck.Value", config.isShowBlockName());
        builder.set("#ShowModNameCheck.Value", config.isShowModName());
        builder.set("#ShowBlockIdCheck.Value", config.isShowBlockId());
        builder.set("#ShowToolEfficiencyCheck.Value", config.isShowToolEfficiency());
        builder.set("#ShowCropInfoCheck.Value", config.isShowCropInfo());
        builder.set("#ShowItemIconCheck.Value", config.isShowItemIcon());


        events.addEventBinding(CustomUIEventBindingType.Activating, "#" + SAVE_BUTTON,
                EventData.of(BUTTON, SAVE_BUTTON)
                        .append("@Show", "#ShowCheck.Value")
                        .append("@ShowBlockName", "#ShowBlockNameCheck.Value")
                        .append("@ShowModName", "#ShowModNameCheck.Value")
                        .append("@ShowBlockId", "#ShowBlockIdCheck.Value")
                        .append("@ShowToolEfficiency", "#ShowToolEfficiencyCheck.Value")
                        .append("@ShowCropInfo", "#ShowCropInfoCheck.Value")
                        .append("@ShowItemIcon", "#ShowItemIconCheck.Value")
        );

        events.addEventBinding(CustomUIEventBindingType.Activating, "#" + CLOSE_BUTTON,
                EventData.of(BUTTON, CLOSE_BUTTON)
        );
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull ConfigData data) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;

        player.getPageManager().setPage(ref, store, Page.None);
        if (!SAVE_BUTTON.equals(data.button)) {
            return;
        }
        player.sendMessage(Message.translation("waila.config.saved"));

        val config = configWrapper.get();
        config.setShow(data.show);
        config.setShowBlockName(data.showBlockName);
        config.setShowModName(data.showModName);
        config.setShowBlockId(data.showBlockId);
        config.setShowToolEfficiency(data.showToolEfficiency);
        config.setShowCropInfo(data.showCropInfo);
        config.setShowItemIcon(data.showItemIcon);

        configWrapper.save();
    }

    public static class ConfigData {
        String button = "";
        boolean show;
        boolean showBlockName;
        boolean showModName;
        boolean showBlockId;
        boolean showToolEfficiency;
        boolean showCropInfo;
        boolean showItemIcon;

        public static final BuilderCodec<ConfigData> CODEC = BuilderCodec.builder(ConfigData.class, ConfigData::new)
                .append(new KeyedCodec<>(BUTTON, Codec.STRING), (d, v) -> d.button = v, d -> d.button).add()
                .append(new KeyedCodec<>("@Show", Codec.BOOLEAN), (d, v) -> d.show = v, d -> d.show).add()
                .append(new KeyedCodec<>("@ShowBlockName", Codec.BOOLEAN), (d, v) -> d.showBlockName = v, d -> d.showBlockName).add()
                .append(new KeyedCodec<>("@ShowModName", Codec.BOOLEAN), (d, v) -> d.showModName = v, d -> d.showModName).add()
                .append(new KeyedCodec<>("@ShowBlockId", Codec.BOOLEAN), (d, v) -> d.showBlockId = v, d -> d.showBlockId).add()
                .append(new KeyedCodec<>("@ShowToolEfficiency", Codec.BOOLEAN), (d, v) -> d.showToolEfficiency = v, d -> d.showToolEfficiency).add()
                .append(new KeyedCodec<>("@ShowCropInfo", Codec.BOOLEAN), (d, v) -> d.showCropInfo = v, d -> d.showCropInfo).add()
                .append(new KeyedCodec<>("@ShowItemIcon", Codec.BOOLEAN), (d, v) -> d.showItemIcon = v, d -> d.showItemIcon).add()
                .build();
    }
}