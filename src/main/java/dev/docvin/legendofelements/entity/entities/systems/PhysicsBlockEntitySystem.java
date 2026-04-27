package dev.docvin.legendofelements.entity.entities.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.ChangeVelocityType;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.entity.entities.components.PhysicsBlockComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PhysicsBlockEntitySystem extends EntityTickingSystem<EntityStore> {

    private static final Query<EntityStore> QUERY = Query.and(PhysicsBlockComponent.getComponentType(), Velocity.getComponentType());
    private static final Vector3f MAX = new Vector3f(0, 1.5F, 0);


    @Override
    public void tick(float delta, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        Velocity velocity = commandBuffer.getComponent(ref, Velocity.getComponentType());
        assert velocity != null;

        double ySpeed = controlSpeed(velocity.getVelocity().y, -0.5F, delta);
        Vector3d s = new Vector3d(0, ySpeed, 0);
        velocity.addInstruction(s, null, ChangeVelocityType.Set);
        TransformComponent transformComponent = commandBuffer.getComponent(ref, TransformComponent.getComponentType());
        assert transformComponent != null;
        Vector3d vector3d = transformComponent.getPosition().clone().add(velocity.getVelocity());
        transformComponent.setPosition(vector3d);
        World world = store.getExternalData().getWorld();
        Vector3i block = vector3d.toVector3i();
        if (world.getBlock(block) != 0) {
            world.execute(() -> {
                commandBuffer.removeEntity(ref, RemoveReason.REMOVE);
                block.add(0, 1, 0);
                world.setBlock(block.x, block.y, block.z, "Sliding_Box");
            });
        }

    }

    private double controlSpeed(double speed, float min, float dt) {
        if (speed <= 0 && speed > min) {
            speed -= 0.2 * dt;
        } else {
            speed += 0.1 * dt;
        }
        return speed;
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return QUERY;
    }
}
