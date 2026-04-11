package dev.docvin.legendofelements.blocks;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

import javax.annotation.Nullable;

public class BlockAnimtionComponent implements Component<ChunkStore> {
    public static final BuilderCodec<BlockAnimtionComponent> CODEC = BuilderCodec.builder(BlockAnimtionComponent.class, BlockAnimtionComponent::new)
            .append(new KeyedCodec<>("AnimationLength", Codec.FLOAT), (c, v) -> c.animationLength = v, c -> c.animationLength)
            .add()
            .build();
    private static ComponentType<ChunkStore, BlockAnimtionComponent> componentType;

    private float animationLength;

    public static ComponentType<ChunkStore, BlockAnimtionComponent> getComponentType() {
        return componentType;
    }

    public static void setComponentType(ComponentType<ChunkStore, BlockAnimtionComponent> componentType) {
        BlockAnimtionComponent.componentType = componentType;
    }

    public float getAnimationLength() {
        return animationLength;
    }

    public void setAnimationLength(float animationLength) {
        this.animationLength = animationLength;
    }

    @Nullable
    @Override
    public Component<ChunkStore> clone() {
        return new BlockAnimtionComponent();
    }
}
