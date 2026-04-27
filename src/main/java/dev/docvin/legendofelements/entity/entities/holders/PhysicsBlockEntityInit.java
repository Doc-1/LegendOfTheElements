package dev.docvin.legendofelements.entity.entities.holders;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.RootDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.HolderSystem;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.entity.entities.components.PhysicsBlockComponent;

import javax.annotation.Nonnull;
import java.util.Set;

public class PhysicsBlockEntityInit extends HolderSystem<EntityStore> {

    private static final Query<EntityStore> QUERY = PhysicsBlockComponent.getComponentType();

    @Override
    public void onEntityAdd(@Nonnull Holder<EntityStore> holder, @Nonnull AddReason addReason, @Nonnull Store<EntityStore> store) {
        holder.putComponent(NetworkId.getComponentType(), new NetworkId(store.getExternalData().takeNextNetworkId()));
        holder.ensureComponent(Velocity.getComponentType());
    }

    @Override
    public void onEntityRemoved(@Nonnull Holder<EntityStore> holder, @Nonnull RemoveReason removeReason, @Nonnull Store<EntityStore> store) {

    }

    @Override
    public Query<EntityStore> getQuery() {
        return QUERY;
    }

    @Nonnull
    @Override
    public Set<Dependency<EntityStore>> getDependencies() {
        return RootDependency.firstSet();
    }
}
