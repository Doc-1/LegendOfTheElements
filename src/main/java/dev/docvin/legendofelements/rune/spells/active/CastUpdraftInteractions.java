package dev.docvin.legendofelements.rune.spells.active;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.ChangeVelocityType;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class CastUpdraftInteractions {

    public static class CastUpdraftFirst extends SimpleInteraction {

        public static final BuilderCodec<CastUpdraftFirst> CODEC = BuilderCodec.builder(
                CastUpdraftFirst.class, CastUpdraftFirst::new, SimpleInteraction.CODEC
        ).build();

        private boolean casted = false;

        @Override
        protected void tick0(boolean firstRun, float time, @Nonnull InteractionType type, @Nonnull InteractionContext interactionContext, @Nonnull CooldownHandler cooldownHandler) {
            if (firstRun)
                casted = false;
            System.out.println(time);
            if (!casted) {
                Ref<EntityStore> ref = interactionContext.getOwningEntity();
                Store<EntityStore> store = ref.getStore();
                MovementStatesComponent movementStatesComponent = store.getComponent(ref, MovementStatesComponent.getComponentType());

                if (movementStatesComponent == null)
                    interactionContext.getServerState().state = InteractionState.Failed;
                else if (movementStatesComponent.getMovementStates().onGround)
                    interactionContext.getServerState().state = InteractionState.Failed;
                else if (time <= 0.2 && !movementStatesComponent.getMovementStates().crouching)
                    this.cast(movementStatesComponent, interactionContext, cooldownHandler);
                super.tick0(firstRun, time, type, interactionContext, cooldownHandler);
            }
        }

        protected void cast(@Nonnull MovementStatesComponent movementStatesComponent, @Nonnull InteractionContext interactionContext, @Nonnull CooldownHandler cooldownHandler) {
            Ref<EntityStore> ref = interactionContext.getOwningEntity();
            Store<EntityStore> store = ref.getStore();

            Velocity velocityComponent = store.getComponent(ref, Velocity.getComponentType());
            TransformComponent transformComponent = store.getComponent(ref, TransformComponent.getComponentType());
            if (velocityComponent == null || transformComponent == null) {
                interactionContext.getServerState().state = InteractionState.Failed;
                return;
            }
            if (velocityComponent.getVelocity().y < -8) {
                interactionContext.getServerState().state = InteractionState.Failed;
                return;
            }
            Vector3d velocity = velocityComponent.getVelocity().clone().normalize();
            velocity.addScaled(new Vector3d(3, 4, 3), 5);
            int index = SoundEvent.getAssetMap().getIndex("SFX_Emit_Wind_Gusts");
            SoundUtil.playSoundEvent3d(index, SoundCategory.SFX, transformComponent.getPosition().x, transformComponent.getPosition().y, transformComponent.getPosition().z, 2.0F, 1.0F, store);

            velocityComponent.addInstruction(velocity, null, ChangeVelocityType.Add);
            casted = true;
        }

    }

    public static class CastUpdraftSecond extends SimpleInstantInteraction {

        public static final BuilderCodec<CastUpdraftSecond> CODEC = BuilderCodec.builder(
                CastUpdraftSecond.class, CastUpdraftSecond::new, SimpleInstantInteraction.CODEC
        ).build();

        @Override
        protected void firstRun(@Nonnull InteractionType interactionType, @Nonnull InteractionContext interactionContext, @Nonnull CooldownHandler cooldownHandler) {

        }
    }
}
