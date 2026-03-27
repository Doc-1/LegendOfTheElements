package dev.docvin.legendofelements.registry;

import com.hypixel.hytale.assetstore.map.IndexedLookupTableAssetMap;
import com.hypixel.hytale.server.core.asset.HytaleAssetStore;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.registry.AssetRegistry;
import dev.docvin.legendofelements.rune.assets.RuneSpellsAsset;

public class AllAssets implements AllRegistries {

    public static void register() {
        AssetRegistry assetRegistry = AllRegistries.getPlugin().getAssetRegistry();
        assetRegistry.register(HytaleAssetStore.builder(RuneSpellsAsset.class, new IndexedLookupTableAssetMap<>(RuneSpellsAsset[]::new))
                .setPath("Runes")
                .setCodec(RuneSpellsAsset.CODEC)
                .setKeyFunction(RuneSpellsAsset::getId)
                .setReplaceOnRemove(RuneSpellsAsset::new)
                .loadsAfter(Interaction.class)
                .build()
        );
    }
}
