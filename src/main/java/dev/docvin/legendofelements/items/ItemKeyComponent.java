package dev.docvin.legendofelements.items;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.chunk.blocks.components.LockableBlockComponent;
import dev.docvin.legendofelements.items.interactions.UseKeyInteraction;
import dev.docvin.legendofelements.registry.data.Component;

/**
 * This is to be used in tandem with {@link LockableBlockComponent} and {@link UseKeyInteraction}
 */
public class ItemKeyComponent implements Component<ItemKeyComponent, EntityStore> {
    public static final BuilderCodec<ItemKeyComponent> CODEC = BuilderCodec.builder(ItemKeyComponent.class, ItemKeyComponent::new)
            .append(new KeyedCodec<>("KeyIds", BuilderCodec.STRING_ARRAY), (c, v) -> c.keyIds = v, c -> c.keyIds)
            .add()
            .append(new KeyedCodec<>("OverrideConsume", BuilderCodec.BOOLEAN), (c, v) -> c.overrideConsume = v, c -> c.overrideConsume)
            .add()
            .build();
    private static ComponentType<EntityStore, ItemKeyComponent> componentType;
    private boolean overrideConsume;
    private String[] keyIds;

    public static ComponentType<EntityStore, ItemKeyComponent> getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType<EntityStore, ItemKeyComponent> staticComponentType) {
        ItemKeyComponent.componentType = staticComponentType;
    }

    @Override
    public String getComponentId() {
        return "Key";
    }

    public boolean isOverrideConsume() {
        return overrideConsume;
    }

    public void setOverrideConsume(boolean overrideConsume) {
        this.overrideConsume = overrideConsume;
    }

    public String[] getKeyIds() {
        return keyIds;
    }

    public void setKeyIds(String[] keyIds) {
        this.keyIds = keyIds;
    }

    @Override
    public ItemKeyComponent clone() {
        ItemKeyComponent itemKeyComponent = new ItemKeyComponent();
        itemKeyComponent.setKeyIds(this.keyIds);
        itemKeyComponent.setOverrideConsume(this.overrideConsume);
        return itemKeyComponent;
    }
}
