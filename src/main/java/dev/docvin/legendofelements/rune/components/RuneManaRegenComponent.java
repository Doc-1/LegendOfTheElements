package dev.docvin.legendofelements.rune.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

public class RuneManaRegenComponent implements Component<EntityStore> {

    public static final BuilderCodec<RuneManaRegenComponent> CODEC =
            BuilderCodec.builder(RuneManaRegenComponent.class, RuneManaRegenComponent::new)
                    .append(new KeyedCodec<>("RegenDelay", Codec.FLOAT),
                            (c, v) -> c.regenDelay = v, c -> c.regenDelay)
                    .add()
                    .append(new KeyedCodec<>("MagicRegen", Codec.INTEGER),
                            (c, v) -> c.magicRegen = v, c -> c.magicRegen)
                    .add()
                    .append(new KeyedCodec<>("ElapsedTime", Codec.FLOAT),
                            (c, v) -> c.elapsedTime = v, c -> c.elapsedTime)
                    .add()
                    .build();

    private static ComponentType<EntityStore, RuneManaRegenComponent> type;

    private float regenDelay;
    private int magicRegen;
    private float elapsedTime;

    public RuneManaRegenComponent() {
        this(1, 0.5F, 0);
    }

    public RuneManaRegenComponent(int magicRegen, float regenDelay, float elapsedTime) {

        this.magicRegen = magicRegen;
        this.regenDelay = regenDelay;
        this.elapsedTime = elapsedTime;
    }

    public static ComponentType<EntityStore, RuneManaRegenComponent> getComponentType() {
        return type;
    }

    public static void setComponentType(ComponentType<EntityStore, RuneManaRegenComponent> componentType) {
        type = componentType;
    }

    public float getRegenDelay() {
        return regenDelay;
    }

    public void setRegenDelay(float regenDelay) {
        this.regenDelay = regenDelay;
    }


    public int getMagicRegen() {
        return magicRegen;
    }

    public void setMagicRegen(int magicRegen) {
        this.magicRegen = magicRegen;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void addElapsedTime(float dt) {
        this.elapsedTime += dt;
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        RuneManaRegenComponent magicComponent = new RuneManaRegenComponent();
        return magicComponent;
    }

}
