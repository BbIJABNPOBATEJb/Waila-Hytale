package me.bbijabnpobatejb.waila.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.val;

/**
 * Represents the HUD element shown on the client screen.
 */
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WailaHud extends CustomUIHud {
    String blockName = "";
    String modName = "";
    String blockId = "";
    Message toolEfficiency = Message.empty();
    String itemIcon = "";
    boolean visible = false;

    public WailaHud(PlayerRef ref) {
        super(ref);
    }

    @Override
    protected void build(UICommandBuilder builder) {
        builder.append("Pages/waila_hud.ui");

        if (!visible) {
            builder.set("#MainContainer.Visible", false);
            return;
        }

        builder.set("#MainContainer.Visible", true);

        updateVisible(builder, "DisplayName", Message.translation(blockName));
        updateVisible(builder, "ModId", "Text", modName);
        updateVisible(builder, "BlockId", "Text", blockId);
        updateVisible(builder, "ToolEfficiency", toolEfficiency);
        updateVisible(builder, "Icon", "ItemId", itemIcon);
    }


    private void updateVisible(UICommandBuilder builder, String key, String text, String value) {
        val visible = !value.isEmpty();
        builder.set("#" + key + ".Visible", visible);
        if (visible) {
            builder.set("#" + key + "." + text, value);
        }
    }

    private void updateVisible(UICommandBuilder builder, String key, Message value) {
        val visible = !value.getAnsiMessage().isEmpty();
        builder.set("#" + key + ".Visible", visible);
        if (visible) {
            builder.set("#" + key + ".Text", value);
        }
    }
}