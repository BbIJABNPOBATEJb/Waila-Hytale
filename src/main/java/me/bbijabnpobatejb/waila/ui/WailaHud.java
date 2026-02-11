package me.bbijabnpobatejb.waila.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.Anchor;
import com.hypixel.hytale.server.core.ui.Value;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import me.bbijabnpobatejb.waila.WailaPlugin;


/**
 * Represents the HUD element shown on the client screen.
 */
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WailaHud extends CustomUIHud {
    public static final String WAILA = "#Waila";
    String blockName = "";
    String modName = "";
    String blockId = "";
    Message toolEfficiency = Message.empty();
    Message cropInfo = Message.empty();
    String itemIcon = "";
    boolean visible = false;

    boolean preview = false;
    boolean mirrorX = false;
    float previewGuiScale = 1f;
    int previewGuiOffsetX = 20;
    int previewGuiOffsetY = 20;

    public WailaHud(PlayerRef ref) {
        super(ref);
    }

    @Override
    protected void build(UICommandBuilder builder) {
        builder.append("Pages/waila_hud.ui");

        if (!visible) {
            visible(builder, false);
            return;
        }
        visible(builder, true);

        updateTransformation(builder);

        updateVisible(builder, "DisplayName", Message.translation(blockName));
        updateVisible(builder, "ModId", "Text", modName);
        updateVisible(builder, "BlockId", "Text", blockId);
        updateVisible(builder, "ToolEfficiency", toolEfficiency);
        updateVisible(builder, "CropInfo", cropInfo);
        updateVisible(builder, "Icon", "ItemId", itemIcon);

        updateBackground(builder);
    }


    private void updateTransformation(UICommandBuilder builder) {
        val cfg = WailaPlugin.get().getConfigWrapper().get();
        val anchor = new Anchor();
        val guiScale = preview ? previewGuiScale : cfg.getHudScale();
        val x = preview ? previewGuiOffsetX : cfg.getHudOffsetX();
        val y = preview ? previewGuiOffsetY : cfg.getHudOffsetY();
        anchor.setTop(Value.of(x));

        val itemAnchor = new Anchor();
        itemAnchor.setWidth(Value.of((int) (guiScale * 32)));
        itemAnchor.setHeight(Value.of((int) (guiScale * 32)));
        itemAnchor.setHorizontal(Value.of((int) (guiScale * 6)));
        itemAnchor.setTop(Value.of(0));
        if (preview ? mirrorX : cfg.isMirrorX()) {
            anchor.setLeft(Value.of(y));
            builder.set(WAILA + "Main.LayoutMode", "Left");
            builder.set(WAILA + "Hud.LayoutMode", "Right");
        } else {
            anchor.setRight(Value.of(y));
            builder.set(WAILA + "Main.LayoutMode", "Right");
            builder.set(WAILA + "Hud.LayoutMode", "Left");
        }
        builder.setObject(WAILA + "Icon.Anchor", itemAnchor);

        anchor.setWidth(Value.of((int) (guiScale * 400)));
        builder.setObject(WAILA + "Main.Anchor", anchor);

        val fontSize = (int) (11 * guiScale);
        builder.set(WAILA + "DisplayName.Style.FontSize", (int) (14 * guiScale));
        builder.set(WAILA + "ModId.Style.FontSize", fontSize);
        builder.set(WAILA + "BlockId.Style.FontSize", fontSize);
        builder.set(WAILA + "ToolEfficiency.Style.FontSize", fontSize);
        builder.set(WAILA + "CropInfo.Style.FontSize", fontSize);
    }

    private void visible(UICommandBuilder builder, boolean b) {
        builder.set(WAILA + "Main.Visible", b);
    }

    private void updateBackground(UICommandBuilder builder) {
        val s = WAILA + "Hud.Background";
        if (cropInfo.getAnsiMessage().isEmpty()) {
            builder.set(s, "Common/TooltipDefaultBackground.png");
            builder.set(s + ".Border", 20);
        } else {
            builder.set(s, "Common/InputBoxSelected.png");
            builder.set(s + ".Border", 16);
        }
        builder.set(s + ".Color", "#ffffffbb");
    }


    private void updateVisible(UICommandBuilder builder, String key, String text, String value) {
        val visible = !value.isEmpty();
        builder.set(WAILA + key + ".Visible", visible);
        if (visible) {
            builder.set(WAILA + key + "." + text, value);
        }
    }

    private void updateVisible(UICommandBuilder builder, String key, Message value) {
        val visible = !value.getAnsiMessage().isEmpty();
        builder.set(WAILA + key + ".Visible", visible);
        if (visible) {
            builder.set(WAILA + key + ".Text", value);
        }
    }

}