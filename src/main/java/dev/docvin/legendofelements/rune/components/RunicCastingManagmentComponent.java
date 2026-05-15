package dev.docvin.legendofelements.rune.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.rune.systems.RunicSpellCastingSystem;

import javax.annotation.Nullable;

/**
 * This component contains the information needed to manage the {@link RunicSpellCastingSystem}.
 */
public class RunicCastingManagmentComponent implements Component<EntityStore> {

    public static final BuilderCodec<RunicCastingManagmentComponent> CODEC = BuilderCodec.builder(RunicCastingManagmentComponent.class, RunicCastingManagmentComponent::new)
            .append(new KeyedCodec<>("MaxMagic", Codec.INTEGER), (c, v) -> c.maxMagic = v, c -> c.maxMagic)
            .add()
            .append(new KeyedCodec<>("CurrentMagic", Codec.INTEGER), (c, v) -> c.currentMagic = v, c -> c.currentMagic)
            .add()
            .append(new KeyedCodec<>("Tick", Codec.FLOAT), (c, v) -> c.tick = v, c -> c.tick)
            .add()
            .build();


    private static ComponentType<EntityStore, RunicCastingManagmentComponent> type;
    private int maxMagic;
    private int currentMagic;
    private boolean casting = false;
    private float tick;
    private int currentSpellCasting = -1;

    public RunicCastingManagmentComponent() {
        this(100, 0);
    }

    public RunicCastingManagmentComponent(int maxMagic, int currentMagic) {
        this.currentMagic = currentMagic;
        this.maxMagic = maxMagic;
    }

    public static ComponentType<EntityStore, RunicCastingManagmentComponent> getComponentType() {
        return type;
    }

    public static void setComponentType(ComponentType<EntityStore, RunicCastingManagmentComponent> componentType) {
        type = componentType;
    }

    public int getCurrentSpellCasting() {
        return currentSpellCasting;
    }

    public void setCurrentSpellCasting(int currentSpellCasting) {
        this.currentSpellCasting = currentSpellCasting;
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
        return new RunicCastingManagmentComponent();
    }
}
