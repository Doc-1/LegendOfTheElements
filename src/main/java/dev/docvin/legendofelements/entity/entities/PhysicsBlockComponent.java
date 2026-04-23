package dev.docvin.legendofelements.entity.entities;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

public class PhysicsBlockComponent implements Component<EntityStore> {
    public static final BuilderCodec<PhysicsBlockComponent> CODEC = BuilderCodec.builder(PhysicsBlockComponent.class, PhysicsBlockComponent::new)
            .append(new KeyedCodec<>("SourceItem", Codec.STRING), (o, v) -> o.sourceItem = v, o -> o.sourceItem)
            .add()
            .build();
    private static ComponentType<EntityStore, PhysicsBlockComponent> componentType;
    private boolean collideWithBlock;
    private String sourceItem;

    public static ComponentType<EntityStore, PhysicsBlockComponent> getComponentType() {
        return componentType;
    }

    public static void setComponentType(ComponentType<EntityStore, PhysicsBlockComponent> componentType) {
        PhysicsBlockComponent.componentType = componentType;
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        return new PhysicsBlockComponent();
    }
}
