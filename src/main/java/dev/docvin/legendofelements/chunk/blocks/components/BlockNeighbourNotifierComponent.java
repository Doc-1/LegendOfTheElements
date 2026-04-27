package dev.docvin.legendofelements.chunk.blocks.components;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

/**
 * This is to be used when block changes need to be announced to the nearby blocks. This is useful for network connections.
 */
public class BlockNeighbourNotifierComponent implements Component<ChunkStore> {

    public static final BuilderCodec<BlockNeighbourNotifierComponent> CODEC = BuilderCodec.builder(BlockNeighbourNotifierComponent.class, BlockNeighbourNotifierComponent::new)
            .append(new KeyedCodec<>("QueueNotification", BuilderCodec.BOOLEAN), (c, v) -> c.queue = v, c -> c.queue)
            .add()
            .build();
    private static ComponentType<ChunkStore, BlockNeighbourNotifierComponent> componentType;
    private boolean queue = false;

    public static ComponentType<ChunkStore, BlockNeighbourNotifierComponent> getComponentType() {
        return componentType;
    }

    public static void setComponentType(ComponentType<ChunkStore, BlockNeighbourNotifierComponent> componentType) {
        BlockNeighbourNotifierComponent.componentType = componentType;
    }

    public boolean isQueued() {
        return queue;
    }

    public void setQueue(boolean queue) {
        this.queue = queue;
    }

    @Override
    public Component<ChunkStore> clone() {
        return new BlockNeighbourNotifierComponent();
    }
}
