package dev.docvin.legendofelements.rune.exception;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import dev.docvin.legendofelements.rune.assets.RuneSpellsAsset;

import javax.annotation.Nonnull;
import java.util.Objects;

public class InvalidElementTypeException extends RuntimeException {

    public InvalidElementTypeException(@Nonnull String message, @Nonnull AssetExtraInfo.Data data) {
        super((Objects.requireNonNull(data.getKey() instanceof String key ? RuneSpellsAsset.getAssetMap().getPath(key) : "data.getKey() is not a String!")).toString());

    }
}
