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
public class RunicSystemComponent implements Component<EntityStore> {

    public static final BuilderCodec<RunicSystemComponent> CODEC = BuilderCodec.builder(RunicSystemComponent.class, RunicSystemComponent::new)
            .append(new KeyedCodec<>("MaxMagic", Codec.INTEGER), (c, v) -> c.maxMagic = v, c -> c.maxMagic)
            .add()
            .append(new KeyedCodec<>("CurrentMagic", Codec.INTEGER), (c, v) -> c.currentMagic = v, c -> c.currentMagic)
            .add()
            .append(new KeyedCodec<>("Tick", Codec.FLOAT), (c, v) -> c.tick = v, c -> c.tick)
            .add()
            .build();


    private static ComponentType<EntityStore, RunicSystemComponent> type;
    private int maxMagic;
    private int currentMagic;
    private boolean casting = false;
    private float tick;

    public RunicSystemComponent() {
        this(100, 0);
    }

    public RunicSystemComponent(int maxMagic, int currentMagic) {
        this.currentMagic = currentMagic;
        this.maxMagic = maxMagic;
    }

    public static ComponentType<EntityStore, RunicSystemComponent> getComponentType() {
        return type;
    }

    public static void setComponentType(ComponentType<EntityStore, RunicSystemComponent> componentType) {
        type = componentType;
    }

    public boolean isCasting() {
        return casting;
    }

    public void setCasting(boolean casting) {
        this.casting = casting;
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

    public void tick(float delta) {
        tick += delta;
    }

    public void resetTick() {
        tick = 0;
    }

    public float getTick() {
        return tick;
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        return new RunicSystemComponent();
    }
}
