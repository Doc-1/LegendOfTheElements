package dev.docvin.legendofelements.registry;

import com.hypixel.hytale.assetstore.JsonAsset;
import com.hypixel.hytale.assetstore.codec.AssetCodecMapCodec;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.command.system.CommandRegistry;
import com.hypixel.hytale.server.core.plugin.registry.CodecMapRegistry;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.LegendOfTheElementsPlugin;

import javax.annotation.Nonnull;

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

}
