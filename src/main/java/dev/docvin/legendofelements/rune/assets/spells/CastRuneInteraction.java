package dev.docvin.legendofelements.rune.assets.spells;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class CastRuneInteraction extends SimpleInteraction {
    public static final BuilderCodec<CastRuneInteraction> CODEC = BuilderCodec.builder(
            CastRuneInteraction.class, CastRuneInteraction::new
    ).build();

    private boolean casted = false;

    @Override
    protected void tick0(boolean firstRun, float time, @Nonnull InteractionType type, @Nonnull InteractionContext interactionContext, @Nonnull CooldownHandler cooldownHandler) {
        if (firstRun)
            casted = false;
        if (!casted) {
            Ref<EntityStore> ref = interactionContext.getOwningEntity();
            Store<EntityStore> store = ref.getStore();
            MovementStatesComponent movementStatesComponent = store.getComponent(ref, MovementStatesComponent.getComponentType());

            if (movementStatesComponent == null || movementStatesComponent.getMovementStates().onGround && movementStatesComponent.getMovementStates().flying)
                interactionContext.getServerState().state = InteractionState.Failed;
            else if (time <= 0.2 && !movementStatesComponent.getMovementStates().crouching)
                this.cast(movementStatesComponent, interactionContext, cooldownHandler);
        }
        super.tick0(firstRun, time, type, interactionContext, cooldownHandler);
    }

    private void cast(MovementStatesComponent movementStatesComponent, InteractionContext interactionContext, CooldownHandler cooldownHandler) {
    }
}
