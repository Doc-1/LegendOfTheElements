package dev.docvin.legendofelements.entity.velocity.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.ChangeVelocityType;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.modules.physics.systems.IVelocityModifyingSystem;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.entity.velocity.components.AccelerateComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AccelerateSystem extends EntityTickingSystem<EntityStore> implements IVelocityModifyingSystem {
    @Nonnull
    private static final Query<EntityStore> QUERY = Query.and(Velocity.getComponentType(), AccelerateComponent.getComponentType(), TransformComponent.getComponentType());

    @Override
    public void tick(float var1, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> r, @Nonnull CommandBuffer<EntityStore> commandBuffer) {

        archetypeChunk.getReferenceTo(index);
        TransformComponent transformComponent = archetypeChunk.getComponent(index, TransformComponent.getComponentType());
        assert transformComponent != null;

        AccelerateComponent accelerateComponent = archetypeChunk.getComponent(index, AccelerateComponent.getComponentType());
        assert accelerateComponent != null;

        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        if (accelerateComponent.getOriginPosition() == null)
            accelerateComponent.setOriginPosition(transformComponent.getPosition().clone());
        else {
            Velocity velocityComponent = archetypeChunk.getComponent(index, Velocity.getComponentType());
            assert velocityComponent != null;

            Vector3d pos = transformComponent.getPosition();
            accelerateComponent.setFinalPosition(pos.clone());
            Vector3d n = accelerateComponent.getVelocity().scale(10);

            System.out.println(velocityComponent.getVelocity());
            System.out.println(n);
            velocityComponent.addInstruction(n, null, ChangeVelocityType.Add);

            commandBuffer.tryRemoveComponent(ref, AccelerateComponent.getComponentType());
        }
    }


    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return QUERY;
    }
}
