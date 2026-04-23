package dev.docvin.legendofelements.block;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

/**
 * Use this component when working with Systems and need to have a bracket of code that should only run upon placement.
 * Meant to help reduce repeat calling of the methods by monitoring when updates should occur on blocks.
 */
public class FirstRunComponent implements Component<ChunkStore> {
    public static final BuilderCodec<FirstRunComponent> CODEC = BuilderCodec.builder(FirstRunComponent.class, FirstRunComponent::new)
            .append(new KeyedCodec<>("Ran", BuilderCodec.BOOLEAN), (c, v) -> c.ran = v, c -> c.ran)
            .add()
            .build();
    private static ComponentType<ChunkStore, FirstRunComponent> componentType;
    private boolean ran = false;

    public static ComponentType<ChunkStore, FirstRunComponent> getComponentType() {
        return FirstRunComponent.componentType;
    }

    public static void setComponentType(ComponentType<ChunkStore, FirstRunComponent> componentType) {
        FirstRunComponent.componentType = componentType;
    }

    public void setRan(boolean ran) {
        this.ran = ran;
    }

    public boolean hasRanOnce() {
        if (!ran) {
            ran = true;
            return false;
        }
        return true;
    }

    @Override
    public Component<ChunkStore> clone() {
        return new FirstRunComponent();
    }
}
