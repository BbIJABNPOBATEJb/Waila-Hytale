package me.bbijabnpobatejb.waila.command;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.val;
import me.bbijabnpobatejb.waila.WailaPlugin;
import me.bbijabnpobatejb.waila.ui.WailaConfigPage;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * Command to open the Waila configuration menu.
 * Usage: /waila
 */
public class WailaCommand extends AbstractPlayerCommand {
    public WailaCommand() {
        super("waila", "Waila configuration");
        requirePermission("waila.command.waila");
    }

    @Override
    protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        val player = store.getComponent(ref, Player.getComponentType());
        assert (player != null);
        val config = WailaPlugin.get().getConfigWrapper();

        player.getPageManager().openCustomPage(
                ref,
                store,
                new WailaConfigPage(playerRef, config)
        );
    }
}