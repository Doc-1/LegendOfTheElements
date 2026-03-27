package dev.docvin.legendofelements.registry;

import dev.docvin.legendofelements.event.events.player.PlayerMovementEvent;
import dev.docvin.legendofelements.event.listeners.player.OnPlayerMovement;

public class AllEvents implements AllRegistries {

    public static void register() {
        AllRegistries.getEventRegistry().register(PlayerMovementEvent.class, new OnPlayerMovement());
    }

}
