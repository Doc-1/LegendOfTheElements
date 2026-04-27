package dev.docvin.legendofelements.chunk.blocks.components;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import dev.docvin.legendofelements.items.interactions.UseKeyInteraction;
import dev.docvin.legendofelements.items.ItemKeyComponent;

import javax.annotation.Nullable;

/**
 * This component stores the data needed to lock the player out of an action from a block. This is to be used in tandem with
 * {@link UseKeyInteraction} and {@link ItemKeyComponent}
 */
public class LockableBlockComponent implements Component<ChunkStore> {
    public static final BuilderCodec<LockableBlockComponent> CODEC = BuilderCodec.builder(LockableBlockComponent.class, LockableBlockComponent::new)
            .append(new KeyedCodec<>("Locked", BuilderCodec.BOOLEAN), (c, v) -> c.locked = v, c -> c.locked)
            .documentation("The state that the 'lock' is currently in.")
            .add()
            .append(new KeyedCodec<>("ConsumeKey", BuilderCodec.BOOLEAN), (c, v) -> c.consumeKey = v, c -> c.consumeKey)
            .documentation("If this block should consume the key upon use.")
            .add()
            .append(new KeyedCodec<>("KeyId", BuilderCodec.STRING), (c, v) -> c.keyId = v, c -> c.keyId)
            .documentation("The name or id the the block look for in a key with a ItemKeyComponent")
            .add()
            .build();
    private static ComponentType<ChunkStore, LockableBlockComponent> componentType;
    private boolean locked;
    private boolean consumeKey;
    private String keyId;

    public static ComponentType<ChunkStore, LockableBlockComponent> getComponentType() {
        return componentType;
    }

    public static void setComponentType(ComponentType<ChunkStore, LockableBlockComponent> componentType) {
        LockableBlockComponent.componentType = componentType;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isConsumeKey() {
        return consumeKey;
    }

    public void setConsumeKey(boolean consumeKey) {
        this.consumeKey = consumeKey;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    @Nullable
    @Override
    public Component<ChunkStore> clone() {
        LockableBlockComponent lockableBlockComponent = new LockableBlockComponent();
        lockableBlockComponent.setLocked(this.isLocked());
        lockableBlockComponent.setKeyId(this.getKeyId());
        lockableBlockComponent.setConsumeKey(this.isConsumeKey());
        return lockableBlockComponent;
    }
}
