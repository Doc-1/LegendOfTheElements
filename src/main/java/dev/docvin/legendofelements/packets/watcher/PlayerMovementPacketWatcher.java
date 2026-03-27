package dev.docvin.legendofelements.packets.watcher;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.event.IEventDispatcher;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.player.ClientMovement;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.io.adapter.PlayerPacketWatcher;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.event.events.player.PlayerMovementEvent;

import java.util.UUID;

public class PlayerMovementPacketWatcher implements PlayerPacketWatcher {

    @Override
    public void accept(PlayerRef playerRef, Packet packet) {
        if (packet instanceof ClientMovement clientMovement) {
            UUID uuid = playerRef.getWorldUuid();
            if (uuid != null) {
                World world = Universe.get().getWorld(uuid);
                if (world != null)
                    world.execute(() -> {
                        Ref<EntityStore> ref = playerRef.getReference();
                        if (ref != null) {
                            Player player = ref.getStore().getComponent(ref, Player.getComponentType());
                            if (player != null) {
                                var ms = clientMovement.movementStates;
                                if (ms != null && ms.crouching && !ms.onGround) {
                                    IEventDispatcher<PlayerMovementEvent, PlayerMovementEvent> dispatcherFor = HytaleServer.get().getEventBus().dispatchFor(PlayerMovementEvent.class);
                                    if (dispatcherFor.hasListener())
                                        dispatcherFor.dispatch(new PlayerMovementEvent(ref, player, clientMovement.movementStates));
                                }
                            }
                        }
                    });
            }
        }
    }
}
