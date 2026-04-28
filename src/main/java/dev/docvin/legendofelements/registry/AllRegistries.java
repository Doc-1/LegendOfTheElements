package dev.docvin.legendofelements.registry;

import com.hypixel.hytale.assetstore.JsonAsset;
import com.hypixel.hytale.assetstore.codec.AssetCodecMapCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.command.system.CommandRegistry;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.registry.CodecMapRegistry;
import com.hypixel.hytale.server.core.universe.world.WorldProvider;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.LegendOfTheElementsPlugin;
import dev.docvin.legendofelements.registry.data.Component;
import dev.docvin.legendofelements.registry.data.IRegistryDataInteractions;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * Just a place to keep all registries in one nice and neat place.
 */
public interface AllRegistries {

    static LegendOfTheElementsPlugin getPlugin() {
        return LegendOfTheElementsPlugin.get();
    }

    static ComponentRegistryProxy<EntityStore> getEntityStoreRegistry() {
        return getPlugin().getEntityStoreRegistry();
    }

    static ComponentRegistryProxy<ChunkStore> getChunkStoreRegistry() {
        return getPlugin().getChunkStoreRegistry();
    }

    static <K, T extends JsonAsset<K>> CodecMapRegistry.Assets<T, ?> getCodecRegistry(@Nonnull AssetCodecMapCodec<K, T> mapCodec) {
        return getPlugin().getCodecRegistry(mapCodec);
    }

    static CommandRegistry getCommandRegistry() {
        return getPlugin().getCommandRegistry();
    }

    static EventRegistry getEventRegistry() {
        return getPlugin().getEventRegistry();
    }

    static <T extends Interaction> void registerInteraction(Supplier<? extends T> supplier) {
        T interaction = supplier.get();
        if (interaction instanceof IRegistryDataInteractions<?> data) {
            getCodecRegistry(Interaction.CODEC).register(data.getRegId(), interaction.getClass(), data.getCodec());
        }
    }

    @SuppressWarnings("unchecked")
    static <T extends com.hypixel.hytale.component.Component<K>, K extends WorldProvider> void registerComponent(Supplier<? extends T> supplier) {
        com.hypixel.hytale.component.Component<K> component = supplier.get();
        if (component instanceof Component<?, ?> data) {
            ComponentRegistryProxy<K> componentRegistryProxy = data.getProviderClass().isAssignableFrom(EntityStore.class) ? (ComponentRegistryProxy<K>) AllRegistries.getEntityStoreRegistry() : (ComponentRegistryProxy<K>) AllRegistries.getChunkStoreRegistry();
            ComponentType<K, T> componentType = componentRegistryProxy.registerComponent((Class<T>) component.getClass(), data.getComponentId(), (BuilderCodec<T>) data.getCodec());
            ((Component<T, K>) data).setComponentType(componentType);
        }
    }


}
