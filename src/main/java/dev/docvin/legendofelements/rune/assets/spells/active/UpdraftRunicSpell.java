package dev.docvin.legendofelements.rune.assets.spells.active;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.ChangeVelocityType;
import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.rune.assets.RunicSpell;

public class UpdraftRunicSpell extends RunicSpell {
    public static final BuilderCodec<UpdraftRunicSpell> CODEC = BuilderCodec.builder(UpdraftRunicSpell.class, UpdraftRunicSpell::new, RunicSpell.SIMPLE_CODEC)
            .build();

    public UpdraftRunicSpell() {
        super("Updraft");
    }

    @Override
    public boolean castSpell(Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();

        Velocity velocityComponent = store.getComponent(ref, Velocity.getComponentType());
        assert velocityComponent != null;

        Vector3d velocity = velocityComponent.getVelocity().clone().normalize();
        velocity.addScaled(new Vector3d(3, 4, 3), 5);
        velocityComponent.addInstruction(velocity, null, ChangeVelocityType.Add);
        return true;
    }

    @Override
    public boolean performanceToCast(Ref<EntityStore> ref, float tick) {
        Store<EntityStore> store = ref.getStore();
        MovementStatesComponent movementStatesComponent = store.getComponent(ref, MovementStatesComponent.getComponentType());
        assert movementStatesComponent != null;
        MovementStates states = movementStatesComponent.getMovementStates();

        return !states.crouching;
    }

    @Override
    public boolean shouldCast(Store<EntityStore> store, Ref<EntityStore> ref) {
        MovementStatesComponent movementStatesComponent = store.getComponent(ref, MovementStatesComponent.getComponentType());
        assert movementStatesComponent != null;
        MovementStates states = movementStatesComponent.getMovementStates();

        Velocity velocityComponent = store.getComponent(ref, Velocity.getComponentType());
        TransformComponent transformComponent = store.getComponent(ref, TransformComponent.getComponentType());
        if (velocityComponent == null || transformComponent == null)
            return false;

        if (velocityComponent.getVelocity().y < -8)
            return false;

        return states.crouching && !states.flying && !states.onGround;
    }


//    @Override
//    public boolean executeSpell(Store<EntityStore> store, Ref<EntityStore> ref, Player player) {
//        MovementStates playerMovementState = Objects.requireNonNull(store.getComponent(ref, MovementStatesComponent.getComponentType())).getMovementStates();
//        return !playerMovementState.onGround && playerMovementState.crouching;
//    }

}
