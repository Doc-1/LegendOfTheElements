package dev.docvin.legendofelements.rune.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

/**
 * A simple component for player entities to keep track of mana or magical energy needed to cast
 * runic spells.
 */
public class RuneManaComponent implements Component<EntityStore> {

    public static final BuilderCodec<RuneManaComponent> CODEC =
            BuilderCodec.builder(RuneManaComponent.class, RuneManaComponent::new)
                    .append(new KeyedCodec<>("MaxMagic", Codec.INTEGER),
                            (c, v) -> c.maxMagic = v, c -> c.maxMagic)
                    .add()
                    .append(new KeyedCodec<>("CurrentMagic", Codec.INTEGER),
                            (c, v) -> c.currentMagic = v, c -> c.currentMagic)
                    .add()
                    .build();


    private static ComponentType<EntityStore, RuneManaComponent> type;
    private int maxMagic;
    private int currentMagic;

    public RuneManaComponent() {
        this(100, 0);
    }

    public RuneManaComponent(int maxMagic, int currentMagic) {
        this.currentMagic = currentMagic;
        this.maxMagic = maxMagic;
    }

    public static ComponentType<EntityStore, RuneManaComponent> getComponentType() {
        return type;
    }

    public static void setComponentType(ComponentType<EntityStore, RuneManaComponent> componentType) {
        type = componentType;
    }

    public int getCurrentMagic() {
        return currentMagic;
    }

    public void setCurrentMagic(int currentMagic) {
        this.currentMagic = Math.clamp(currentMagic, 0, this.maxMagic);
    }

    public int getMaxMagic() {
        return maxMagic;
    }

    public void setMaxMagic(int maxMagic) {
        this.maxMagic = maxMagic;
    }

    public void regenMagic(int magicRegen) {
        this.setCurrentMagic(this.currentMagic + magicRegen);
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        return null;
    }
}
