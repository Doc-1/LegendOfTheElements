package dev.docvin.legendofelements.event.events.player;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class PlayerMovementEvent extends PlayerEvent<Void> {
    private final @Nonnull MovementStates movementStates;

    public PlayerMovementEvent(@Nonnull Ref<EntityStore> playerRef, @Nonnull Player player, @Nonnull MovementStates movementStates) {
        super(playerRef, player);
        this.movementStates = movementStates;
    }

    @Nonnull
    public MovementStates getMovementStates() {
        return movementStates;
    }
}
