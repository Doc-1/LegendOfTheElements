package dev.docvin.legendofelements.block;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

import javax.annotation.Nullable;

/**
 * Use this component to keep track of duration of block animation. Is used to get around an issue I was having
 * when using run time in the interaction, it prevented more than one of the same interaction to run at the sametime.
 */
public class BlockAnimtionComponent implements Component<ChunkStore> {
    public static final BuilderCodec<BlockAnimtionComponent> CODEC = BuilderCodec.builder(BlockAnimtionComponent.class, BlockAnimtionComponent::new)
            .append(new KeyedCodec<>("AnimationLength", Codec.FLOAT), (c, v) -> c.animationLength = v, c -> c.animationLength)
            .add()
            .append(new KeyedCodec<>("AnimationID", Codec.STRING), (c, v) -> c.animationID = v, c -> c.animationID)
            .add()
            .build();
    private static ComponentType<ChunkStore, BlockAnimtionComponent> componentType;

    private float animationLength;
    private String animationID;
    private float animationTick;

    public static ComponentType<ChunkStore, BlockAnimtionComponent> getComponentType() {
        return componentType;
    }

    public static void setComponentType(ComponentType<ChunkStore, BlockAnimtionComponent> componentType) {
        BlockAnimtionComponent.componentType = componentType;
    }

    public void tickAnimation(float delta) {
        if (animationTick < animationLength)
            animationTick += delta;
    }

    public boolean animationCompleted() {
        return animationLength <= animationTick;
    }

    public float getAnimationLength() {
        return animationLength;
    }

    public void setAnimationLength(float animationLength) {
        this.animationLength = animationLength;
    }

    public String getAnimationID() {
        return animationID;
    }

    public void setAnimationID(String animationID) {
        this.animationID = animationID;
    }

    @Nullable
    @Override
    public Component<ChunkStore> clone() {
        return new BlockAnimtionComponent();
    }
}
