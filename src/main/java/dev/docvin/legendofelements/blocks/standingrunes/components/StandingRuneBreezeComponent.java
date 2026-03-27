package dev.docvin.legendofelements.blocks.standingrunes.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.math.shape.Box;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

import javax.annotation.Nullable;

public class StandingRuneBreezeComponent implements Component<ChunkStore> {
    public static final BuilderCodec<StandingRuneBreezeComponent> CODEC = BuilderCodec.builder(StandingRuneBreezeComponent.class, StandingRuneBreezeComponent::new)
            .append(new KeyedCodec<>("Speed_Modifier", Codec.FLOAT), (c, v) -> c.modifier = v, c -> c.modifier)
            .add()
            .append(new KeyedCodec<>("Distance", Codec.DOUBLE), (c, v) -> c.distance = v, c -> c.distance)
            .add()
            .append(new KeyedCodec<>("Collision_Area", Box.CODEC), (c, v) -> c.collisionArea = v, c -> c.collisionArea)
            .add()
            .append(new KeyedCodec<>("Velocity", Vector3d.CODEC), (c, v) -> c.velocity = v, c -> c.velocity)
            .add()
            .build();

    private static ComponentType<ChunkStore, StandingRuneBreezeComponent> componentType;
    private float modifier;
    private double distance;
    private Box collisionArea;
    private Vector3d velocity;

    public StandingRuneBreezeComponent() {
    }

    public static ComponentType<ChunkStore, StandingRuneBreezeComponent> getComponentType() {
        return componentType;
    }

    public static void setComponentType(ComponentType<ChunkStore, StandingRuneBreezeComponent> componentType) {
        StandingRuneBreezeComponent.componentType = componentType;
    }

    public float getModifier() {
        return modifier;
    }

    public void setModifier(float modifier) {
        this.modifier = modifier;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Box getCollisionArea() {
        return collisionArea;
    }

    public void setCollisionArea(Box collisionArea) {
        this.collisionArea = collisionArea;
    }

    public Vector3d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
    }

    @Nullable
    public Component<ChunkStore> clone() {
        return new StandingRuneBreezeComponent();
    }
}
