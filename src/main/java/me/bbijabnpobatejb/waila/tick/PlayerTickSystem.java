package me.bbijabnpobatejb.waila.tick;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import me.bbijabnpobatejb.waila.WailaPlugin;

import javax.annotation.Nonnull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerTickSystem extends EntityTickingSystem<EntityStore> {
    Query<EntityStore> query;


    public PlayerTickSystem() {
        this.query = Query.and(Player.getComponentType());
    }

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        val holder = EntityUtils.toHolder(index, archetypeChunk);
        val player = holder.getComponent(Player.getComponentType());
        val playerRef = holder.getComponent(PlayerRef.getComponentType());
        if (player == null || playerRef == null) {
            return;
        }
        WailaPlugin.get().getWailaHudService().onTick(player, playerRef,index,archetypeChunk,commandBuffer);
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return query;
    }
}
