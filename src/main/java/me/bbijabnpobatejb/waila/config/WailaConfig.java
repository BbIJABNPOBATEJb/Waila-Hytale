package me.bbijabnpobatejb.waila.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Main configuration class for Waila settings.
 * Stores boolean flags for HUD elements and raycast distance.
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WailaConfig {
    boolean show;
    boolean mirrorX;
    boolean showBlockName;
    boolean showModName;
    boolean showBlockId;
    boolean showToolEfficiency;
    boolean showCropInfo;
    boolean showItemIcon;
    double raycastDistance;
    float hudScale;
    int hudOffsetX;
    int hudOffsetY;

    // Codec for serialization/deserialization of the config
    public static final BuilderCodec<WailaConfig> CODEC = BuilderCodec.builder(WailaConfig.class, WailaConfig::new)
            .append(new KeyedCodec<>("Show", Codec.BOOLEAN),
                    (config, b, _) -> config.show = b,
                    (config, _) -> config.show).add()
            .append(new KeyedCodec<>("MirrorX", Codec.BOOLEAN),
                    (config, b, _) -> config.mirrorX = b,
                    (config, _) -> config.mirrorX).add()
            .append(new KeyedCodec<>("ShowBlockName", Codec.BOOLEAN),
                    (config, b, _) -> config.showBlockName = b,
                    (config, _) -> config.showBlockName).add()
            .append(new KeyedCodec<>("ShowModName", Codec.BOOLEAN),
                    (config, b, _) -> config.showModName = b,
                    (config, _) -> config.showModName).add()
            .append(new KeyedCodec<>("ShowBlockId", Codec.BOOLEAN),
                    (config, b, _) -> config.showBlockId = b,
                    (config, _) -> config.showBlockId).add()
            .append(new KeyedCodec<>("ShowToolEfficiency", Codec.BOOLEAN),
                    (config, b, _) -> config.showToolEfficiency = b,
                    (config, _) -> config.showToolEfficiency).add()
            .append(new KeyedCodec<>("ShowCropInfo", Codec.BOOLEAN),
                    (config, b, _) -> config.showCropInfo = b,
                    (config, _) -> config.showCropInfo).add()
            .append(new KeyedCodec<>("ShowItemIcon", Codec.BOOLEAN),
                    (config, b, _) -> config.showItemIcon = b,
                    (config, _) -> config.showItemIcon).add()
            .append(new KeyedCodec<>("RaycastDistance", Codec.DOUBLE),
                    (config, d, _) -> config.raycastDistance = d,
                    (config, _) -> config.raycastDistance).add()
            .append(new KeyedCodec<>("HudScale", Codec.FLOAT),
                    (config, f, _) -> config.hudScale = f,
                    (config, _) -> config.hudScale).add()
            .append(new KeyedCodec<>("HudOffsetX", Codec.INTEGER),
                    (config, i, _) -> config.hudOffsetX = i,
                    (config, _) -> config.hudOffsetX).add()
            .append(new KeyedCodec<>("HudOffsetY", Codec.INTEGER),
                    (config, i, _) -> config.hudOffsetY = i,
                    (config, _) -> config.hudOffsetX).add()
            .build();

    /**
     * Default constructor with preset values.
     */
    public WailaConfig() {
        this(true, false, true, true, false, true, true, true, 5.0, 1f, 20, 20);
    }
}