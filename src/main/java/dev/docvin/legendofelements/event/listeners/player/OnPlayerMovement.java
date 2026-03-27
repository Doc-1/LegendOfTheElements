package dev.docvin.legendofelements.event.listeners.player;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.entity.velocity.components.AccelerateComponent;
import dev.docvin.legendofelements.event.events.player.PlayerMovementEvent;

import java.util.function.Consumer;

public class OnPlayerMovement implements Consumer<PlayerMovementEvent> {

    @Override
    public void accept(PlayerMovementEvent event) {
        Ref<EntityStore> ref = event.getPlayerRef();
        if (ref.getStore().getComponent(ref, AccelerateComponent.getComponentType()) == null) {
            ref.getStore().addComponent(ref, AccelerateComponent.getComponentType(), new AccelerateComponent());
        }
    }
}
