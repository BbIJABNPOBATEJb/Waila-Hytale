package me.bbijabnpobatejb.waila.command;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import lombok.val;
import me.bbijabnpobatejb.waila.WailaPlugin;
import me.bbijabnpobatejb.waila.ui.WailaConfigPage;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * Command to open the Waila configuration menu.
 * Usage: /waila
 */
public class WailaCommand extends AbstractCommand {
    public WailaCommand() {
        super("waila", "Waila configuration");
        requirePermission("waila.command.waila");
    }

    @Override
    protected CompletableFuture<Void> execute(@Nonnull CommandContext context) {
        val sender = context.sender();
        if (sender instanceof Player player) {
            // Execute on the world thread to ensure thread safety when accessing components
            player.getWorld().execute(() -> {
                val ref = player.getReference();
                val store = ref.getStore();
                val playerRefComponent = store.getComponent(ref, PlayerRef.getComponentType());
                val config = WailaPlugin.get().getConfigWrapper();

                player.getPageManager().openCustomPage(
                        player.getReference(),
                        store,
                        new WailaConfigPage(playerRefComponent, config)
                );
            });
        }
        return CompletableFuture.completedFuture(null);
    }
}