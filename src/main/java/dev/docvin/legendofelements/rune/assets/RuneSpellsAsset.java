package dev.docvin.legendofelements.rune.assets;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.IndexedLookupTableAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;
import com.hypixel.hytale.codec.codecs.map.EnumMapCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.codec.schema.metadata.ui.UIEditorSectionStart;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.common.util.MapUtil;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.modules.interaction.interaction.UnarmedInteractions;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.InteractionConfiguration;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.RootInteraction;
import dev.docvin.legendofelements.rune.elements.RuneElementType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class RuneSpellsAsset implements JsonAssetWithMap<String, IndexedLookupTableAssetMap<String, RuneSpellsAsset>> {

    public static final AssetBuilderCodec<String, RuneSpellsAsset> CODEC = AssetBuilderCodec.builder(
                    RuneSpellsAsset.class,
                    RuneSpellsAsset::new,
                    Codec.STRING,
                    (o, v) -> o.id = v,
                    RuneSpellsAsset::getId,
                    (o, data) -> o.data = data,
                    o -> o.data)
            .append(new KeyedCodec<>("SpellName", Codec.STRING),
                    (c, v) -> c.name = v, c -> c.name)
            .add()
            .append(new KeyedCodec<>("ElementType", new EnumCodec<>(RuneElementType.class).documentKey(RuneElementType.WIND, "Wind Element")),
                    RuneSpellsAsset::setElementType, c -> c.elementType)
            .add()
            .append(new KeyedCodec<>("CastCost", Codec.INTEGER, true), RuneSpellsAsset::setCastCost, c -> c.castCost)
            .add()
            .append(new KeyedCodec<>("CastDelay", Codec.FLOAT),
                    (c, v) -> c.castDelay = v, c -> c.castDelay)
            .add()
            .append(new KeyedCodec<>("RecastDelay", Codec.FLOAT),
                    (c, v) -> c.recastDelay = v, c -> c.recastDelay)
            .add()
            .append(new KeyedCodec<>("ConstantCast", Codec.BOOLEAN),
                    (c, v) -> c.constantCast = v, c -> c.constantCast)
            .add()
            .<Map<InteractionType, String>>appendInherited(
                    new KeyedCodec<>("Interactions", new EnumMapCodec<>(InteractionType.class, RootInteraction.CHILD_ASSET_CODEC)),
                    (item, v) -> item.interactions = MapUtil.combineUnmodifiable(item.interactions, v, () -> new EnumMap<>(InteractionType.class)),
                    item -> item.interactions,
                    (item, parent) -> item.interactions = parent.interactions
            )
            .addValidator(RootInteraction.VALIDATOR_CACHE.getMapValueValidator())
            .metadata(new UIEditorSectionStart("Interactions"))
            .add()
            .<InteractionConfiguration>appendInherited(
                    new KeyedCodec<>("InteractionConfig", InteractionConfiguration.CODEC),
                    (item, v) -> item.interactionConfig = v,
                    item -> item.interactionConfig,
                    (item, parent) -> item.interactionConfig = parent.interactionConfig
            )
            .addValidator(Validators.nonNull())
            .add()
            .<Map<String, String>>appendInherited(
                    new KeyedCodec<>("InteractionVars", new MapCodec<>(RootInteraction.CHILD_ASSET_CODEC, HashMap<String, String>::new)),
                    (item, v) -> item.interactionVars = MapUtil.combineUnmodifiable(item.interactionVars, v),
                    item -> item.interactionVars,
                    (item, parent) -> item.interactionVars = parent.interactionVars
            )
            .addValidator(RootInteraction.VALIDATOR_CACHE.getMapValueValidator())
            .add()
            .afterDecode(RuneSpellsAsset::processConfig)
            .build();
    private static AssetStore<String, RuneSpellsAsset, IndexedLookupTableAssetMap<String, RuneSpellsAsset>> ASSET_STORE;
    protected Map<String, String> interactionVars = Collections.emptyMap();
    protected InteractionConfiguration interactionConfig;
    protected Map<InteractionType, String> interactions = Collections.emptyMap();
    private String name;
    private String elementTypeName;
    private RuneElementType elementType;
    private int castCost;
    private float castDelay;
    private float recastDelay;
    private boolean constantCast;
    private String id;
    private AssetExtraInfo.Data data;

    public RuneSpellsAsset(String id) {
        this.id = id;
    }

    protected RuneSpellsAsset() {

    }

    public static AssetStore<String, RuneSpellsAsset, IndexedLookupTableAssetMap<String, RuneSpellsAsset>> getAssetStore() {
        if (ASSET_STORE == null) {
            ASSET_STORE = AssetRegistry.getAssetStore(RuneSpellsAsset.class);
        }

        return ASSET_STORE;
    }

    public static DefaultAssetMap<String, RuneSpellsAsset> getAssetMap() {
        return getAssetStore().getAssetMap();
    }

    private void processConfig() {
        DefaultAssetMap<String, UnarmedInteractions> unarmedInteractionsAssetMap = UnarmedInteractions.getAssetMap();

        Map<InteractionType, String> interactions = this.interactions.isEmpty() ? new EnumMap<>(InteractionType.class) : new EnumMap<>(this.interactions);
        UnarmedInteractions defaultUnarmedInteractions = unarmedInteractionsAssetMap.getAsset("Empty");
        if (defaultUnarmedInteractions != null) {
            for (Map.Entry<InteractionType, String> entry : defaultUnarmedInteractions.getInteractions().entrySet()) {
                interactions.putIfAbsent(entry.getKey(), entry.getValue());
            }
        }
        this.interactions = Collections.unmodifiableMap(interactions);

        if (this.interactionConfig == null)
            this.interactionConfig = InteractionConfiguration.DEFAULT;


    }

    private String getElementTypeName() {
        return elementTypeName;
    }

    public RuneElementType getElementType() {
        return elementType;
    }

    private void setElementType(String s) {
        for (RuneElementType runeElementType : RuneElementType.values()) {
            if (runeElementType.getTypeName().equals(s)) {
                elementTypeName = s;
                elementType = runeElementType;
                break;
            }
        }
    }

    public void setElementType(RuneElementType elementType) {
        this.elementType = elementType;
    }

    public float getCastDelay() {
        return castDelay;
    }

    public void setCastDelay(float castDelay) {
        this.castDelay = castDelay;
    }

    public float getRecastDelay() {
        return recastDelay;
    }

    public void setRecastDelay(float recastDelay) {
        this.recastDelay = recastDelay;
    }

    public boolean isConstantCast() {
        return constantCast;
    }

    public void setConstantCast(boolean constantCast) {
        this.constantCast = constantCast;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCastCost() {
        return this.castCost;
    }

    public void setCastCost(int castCost) {
        this.castCost = castCost;
    }

    private void setCastCost(Integer castCost) {
        this.castCost = Math.max(0, castCost);
    }

    public Map<String, String> getInteractionVars() {
        return interactionVars;
    }

    public void setInteractionVars(Map<String, String> interactionVars) {
        this.interactionVars = interactionVars;
    }

    public void setInteractionConfig(InteractionConfiguration interactionConfig) {
        this.interactionConfig = interactionConfig;
    }

    public Map<InteractionType, String> getInteractions() {
        return interactions;
    }

    public void setInteractions(Map<InteractionType, String> interactions) {
        this.interactions = interactions;
    }

    @Override
    public String getId() {
        return id;
    }

    public Map<String, String> getInteractionVars(InteractionContext interactionContext) {
        return this.interactionVars;
    }
}
