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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import me.bbijabnpobatejb.waila.WailaPlugin;
import me.bbijabnpobatejb.waila.config.WailaConfig;
import me.bbijabnpobatejb.waila.util.StringUtil;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;

/**
 * Represents the interactive configuration page for Waila settings.
 * <p>
 * This class handles:
 * <ul>
 *     <li>Building the UI based on current config values.</li>
 *     <li>Binding UI events (buttons, sliders, checkboxes) to data keys.</li>
 *     <li>Processing user actions (Save, Reset, Cancel).</li>
 *     <li>Updating the live HUD preview when visual settings are modified.</li>
 * </ul>
 */
@FieldDefaults(level = PRIVATE)
public class WailaConfigPage extends InteractiveCustomUIPage<WailaConfigPage.ConfigData> {

    static final String KEY_ACTION = "Action";

    @Getter
    @RequiredArgsConstructor
    @FieldDefaults(level = PRIVATE, makeFinal = true)
    enum UiAction {
        SAVE("SaveButton"),
        CANCEL("CancelButton"),
        RESET("ResetButton"),
        MIRROR_X("MirrorX"),
        HUD_SCALE("HudScale"),
        HUD_OFFSET_X("HudOffsetX"),
        HUD_OFFSET_Y("HudOffsetY"),
        UNKNOWN("Unknown");

        String uiId;

        static UiAction fromString(String id) {
            return Arrays.stream(values())
                    .filter(a -> a.uiId.equals(id))
                    .findFirst()
                    .orElse(UNKNOWN);
        }
    }

    @Getter
    @RequiredArgsConstructor
    @FieldDefaults(level = PRIVATE, makeFinal = true)
    enum BooleanSetting {
        SHOW("Show", WailaConfig::isShow, (d, v) -> d.show = v),
        MIRROR_X("MirrorX", WailaConfig::isMirrorX, (d, v) -> d.mirrorX = v),
        BLOCK_NAME("ShowBlockName", WailaConfig::isShowBlockName, (d, v) -> d.showBlockName = v),
        MOD_NAME("ShowModName", WailaConfig::isShowModName, (d, v) -> d.showModName = v),
        BLOCK_ID("ShowBlockId", WailaConfig::isShowBlockId, (d, v) -> d.showBlockId = v),
        TOOL_EFFICIENCY("ShowToolEfficiency", WailaConfig::isShowToolEfficiency, (d, v) -> d.showToolEfficiency = v),
        CROP_INFO("ShowCropInfo", WailaConfig::isShowCropInfo, (d, v) -> d.showCropInfo = v),
        ITEM_ICON("ShowItemIcon", WailaConfig::isShowItemIcon, (d, v) -> d.showItemIcon = v);

        String baseId;
        Function<WailaConfig, Boolean> configGetter;
        BiConsumer<ConfigData, Boolean> dataSetter; // Используется при построении кодека

        public String getUiCheckBoxValue() {
            return "#" + baseId + " #CheckBox.Value";
        }

        public String getDataKey() {
            return "@" + baseId;
        }
    }

    final Config<WailaConfig> configWrapper;

    boolean mirrorX;
    float hudScale;
    int hudOffsetX;
    int hudOffsetY;

    public WailaConfigPage(PlayerRef playerRef, Config<WailaConfig> configWrapper) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, ConfigData.CODEC);
        this.configWrapper = configWrapper;
        val cfg = configWrapper.get();
        this.mirrorX = cfg.isMirrorX();
        this.hudScale = cfg.getHudScale();
        this.hudOffsetX = cfg.getHudOffsetX();
        this.hudOffsetY = cfg.getHudOffsetY();
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder builder, @Nonnull UIEventBuilder events, @Nonnull Store<EntityStore> store) {
        builder.append("Pages/waila_config.ui");
        val config = configWrapper.get();

        for (BooleanSetting setting : BooleanSetting.values()) {
            builder.set(setting.getUiCheckBoxValue(), setting.configGetter.apply(config));
        }

        setupSlider(builder, UiAction.HUD_SCALE, config.getHudScale(), StringUtil.fromFloat(config.getHudScale(), 1));
        setupSlider(builder, UiAction.HUD_OFFSET_X, config.getHudOffsetX(), String.valueOf(config.getHudOffsetX()));
        setupSlider(builder, UiAction.HUD_OFFSET_Y, config.getHudOffsetY(), String.valueOf(config.getHudOffsetY()));

        val saveEventData = EventData.of(KEY_ACTION, UiAction.SAVE.uiId);

        for (val setting : BooleanSetting.values()) {
            saveEventData.append(setting.getDataKey(), setting.getUiCheckBoxValue());
        }
        saveEventData.append("@HudScale", sliderVal(UiAction.HUD_SCALE))
                .append("@HudOffsetX", sliderVal(UiAction.HUD_OFFSET_X))
                .append("@HudOffsetY", sliderVal(UiAction.HUD_OFFSET_Y));

        events.addEventBinding(CustomUIEventBindingType.Activating, buttonId(UiAction.SAVE), saveEventData);

        events.addEventBinding(CustomUIEventBindingType.Activating, buttonId(UiAction.CANCEL),
                EventData.of(KEY_ACTION, UiAction.CANCEL.uiId));

        events.addEventBinding(CustomUIEventBindingType.Activating, buttonId(UiAction.RESET),
                EventData.of(KEY_ACTION, UiAction.RESET.uiId));

        bindCheckBoxChange(events, UiAction.MIRROR_X, "@MirrorX");

        bindSliderChange(events, UiAction.HUD_SCALE, "@HudScale");
        bindSliderChange(events, UiAction.HUD_OFFSET_X, "@HudOffsetX");
        bindSliderChange(events, UiAction.HUD_OFFSET_Y, "@HudOffsetY");
    }

    private void bindCheckBoxChange(UIEventBuilder events, UiAction action, String dataKey) {
        events.addEventBinding(CustomUIEventBindingType.ValueChanged, "#" + action.uiId + " #CheckBox",
                new EventData()
                        .append(KEY_ACTION, action.uiId)
                        .append(dataKey, "#" + action.uiId + " #CheckBox.Value"),
                false);
    }


    private void setupSlider(UICommandBuilder builder, UiAction action, Number value, String textValue) {
        if (value instanceof Float f) {
            builder.set("#" + action.uiId + " #Slider.Value", f);
        } else if (value instanceof Integer i) {
            builder.set("#" + action.uiId + " #Slider.Value", i);
        }
        builder.set("#" + action.uiId + " #TextValue.Text", textValue);
    }


    private void bindSliderChange(UIEventBuilder events, UiAction action, String dataKey) {
        events.addEventBinding(CustomUIEventBindingType.ValueChanged, "#" + action.uiId + " #Slider",
                new EventData()
                        .append(KEY_ACTION, action.uiId)
                        .append(dataKey, sliderVal(action)),
                false);
    }

    private String buttonId(UiAction action) {
        return "#" + action.uiId;
    }

    private String sliderVal(UiAction action) {
        return "#" + action.uiId + " #Slider.Value";
    }

    @Override
    protected void close() {
        updatePreview(false);
        super.close();
    }

    /**
     * Processes events sent from the client UI.
     * Handles logic for saving configuration, resetting to defaults,
     * or updating the live preview when sliders/toggles are changed.
     *
     * @param ref   The entity reference (player).
     * @param store The component store.
     * @param data  The decoded data sent from the UI (action ID, toggle states, slider values).
     */
    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull ConfigData data) {


        val action = UiAction.fromString(data.actionId);

        switch (action) {
            case SAVE -> {
                updatePreview(false);
                playerRef.sendMessage(Message.translation("waila.config.saved"));

                val config = configWrapper.get();

                config.setShow(data.show);
                config.setMirrorX(data.mirrorX);
                config.setShowBlockName(data.showBlockName);
                config.setShowModName(data.showModName);
                config.setShowBlockId(data.showBlockId);
                config.setShowToolEfficiency(data.showToolEfficiency);
                config.setShowCropInfo(data.showCropInfo);
                config.setShowItemIcon(data.showItemIcon);

                config.setHudScale(data.hudScale);
                config.setHudOffsetX(data.hudOffsetX);
                config.setHudOffsetY(data.hudOffsetY);

                configWrapper.save();
                close();
            }
            case CANCEL -> {
                updatePreview(false);
                close();
            }
            case RESET -> {
                playerRef.sendMessage(Message.translation("waila.config.reseted"));
                WailaPlugin.get().resetConfig();
                updatePreview(false);
                close();
            }
            case MIRROR_X -> {
                this.mirrorX = data.mirrorX;
                updatePreview(true);
            }
            case HUD_SCALE -> updateSlider(data.hudScale, UiAction.HUD_SCALE, v -> {
                this.hudScale = v;
                return StringUtil.fromFloat(v, 1);
            });
            case HUD_OFFSET_X -> updateSlider((float) data.hudOffsetX, UiAction.HUD_OFFSET_X, v -> {
                this.hudOffsetX = v.intValue();
                return String.valueOf(v.intValue());
            });
            case HUD_OFFSET_Y -> updateSlider((float) data.hudOffsetY, UiAction.HUD_OFFSET_Y, v -> {
                this.hudOffsetY = v.intValue();
                return String.valueOf(v.intValue());
            });
        }
    }


    private void updateSlider(float value, UiAction action, Function<Float, String> textFormatter) {
        val builder = new UICommandBuilder();
        String text = textFormatter.apply(value);
        builder.set("#" + action.uiId + " #TextValue.Text", text);
        this.sendUpdate(builder, null, false);
        updatePreview(true);
    }

    private void updatePreview(boolean preview) {
        WailaPlugin.get().getWailaHudService().updatePreview(playerRef, preview, mirrorX, hudScale, hudOffsetX, hudOffsetY);
    }


    @FieldDefaults(level = PRIVATE)
    public static class ConfigData {
        String actionId = "";
        boolean show;
        boolean mirrorX;
        boolean showBlockName;
        boolean showModName;
        boolean showBlockId;
        boolean showToolEfficiency;
        boolean showCropInfo;
        boolean showItemIcon;
        float hudScale;
        int hudOffsetX;
        int hudOffsetY;

        public static final BuilderCodec<ConfigData> CODEC;

        static {
            val builder = BuilderCodec.builder(ConfigData.class, ConfigData::new)
                    .append(new KeyedCodec<>(KEY_ACTION, Codec.STRING), (d, v) -> d.actionId = v, d -> d.actionId).add();

            for (val setting : BooleanSetting.values()) {
                builder.append(new KeyedCodec<>(setting.getDataKey(), Codec.BOOLEAN),
                        setting.dataSetter,
                        d -> switch (setting) {
                            case SHOW -> d.show;
                            case BLOCK_NAME -> d.showBlockName;
                            case MIRROR_X -> d.mirrorX;
                            case MOD_NAME -> d.showModName;
                            case BLOCK_ID -> d.showBlockId;
                            case TOOL_EFFICIENCY -> d.showToolEfficiency;
                            case CROP_INFO -> d.showCropInfo;
                            case ITEM_ICON -> d.showItemIcon;
                        }
                ).add();
            }

            builder
                    .append(new KeyedCodec<>("@HudScale", Codec.FLOAT), (d, v) -> d.hudScale = v, d -> d.hudScale).add()
                    .append(new KeyedCodec<>("@HudOffsetX", Codec.INTEGER), (d, v) -> d.hudOffsetX = v, d -> d.hudOffsetX).add()
                    .append(new KeyedCodec<>("@HudOffsetY", Codec.INTEGER), (d, v) -> d.hudOffsetY = v, d -> d.hudOffsetY).add();

            CODEC = builder.build();
        }
    }
}