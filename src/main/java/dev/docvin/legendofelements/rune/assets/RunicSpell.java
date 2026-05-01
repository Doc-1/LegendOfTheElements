package dev.docvin.legendofelements.rune.assets;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.assetstore.codec.AssetCodecMapCodec;
import com.hypixel.hytale.assetstore.codec.ContainedAssetCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.IndexedLookupTableAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

/**
 * Tried to used Interactions as the base to "cast" the spells however, the interactions where too limited when setting values
 * to runtime or cooldown. It prevented other Interactions from occurring. These perform a very similar purpose as Interactions
 * but only for controlling the logic of rune spells.
 */
//todo create a Codec for RunicSpell to assign them to individual players.
public class RunicSpell implements JsonAssetWithMap<String, IndexedLookupTableAssetMap<String, RunicSpell>> {

    public static final AssetCodecMapCodec<String, RunicSpell> CODEC = new AssetCodecMapCodec<>(
            Codec.STRING, (t, k) -> t.id = k, t -> t.id, (t, data) -> t.data = data, t -> t.data
    );


    @Nonnull
    public static final Codec<String> CHILD_ASSET_CODEC = new ContainedAssetCodec<>(RunicSpell.class, CODEC);
    @Nonnull
    public static final BuilderCodec<RunicSpell> SPELL_CODEC = BuilderCodec.builder(RunicSpell.class, RunicSpell::new)
            .append(new KeyedCodec<>("SpellName", Codec.STRING),
                    (c, v) -> c.spellName = v, c -> c.spellName)
            .add()
            .append(new KeyedCodec<>("ElementType", new EnumCodec<>(RuneElementType.class).documentKey(RuneElementType.WIND, "Wind Element")), RunicSpell::setElementType, c -> c.elementType)
            .add()
            .append(new KeyedCodec<>("CastCost", Codec.INTEGER, true), RunicSpell::setCastCost, c -> c.castCost)
            .add()
            .append(new KeyedCodec<>("CastDelay", Codec.FLOAT), (c, v) -> c.castDelay = v, c -> c.castDelay)
            .add()
            .append(new KeyedCodec<>("RecastDelay", Codec.FLOAT), (c, v) -> c.recastDelay = v, c -> c.recastDelay)
            .add()
            .append(new KeyedCodec<>("ConstantCast", Codec.BOOLEAN), (c, v) -> c.constantCast = v, c -> c.constantCast)
            .add()
            .append(new KeyedCodec<>("LifeTime", Codec.FLOAT), (c, v) -> c.lifeTime = v, c -> c.lifeTime)
            .add()
            .build();
    @Nonnull
    public static final Codec<RunicSpell[]> ARRAY_CODEC = new ArrayCodec<>(SPELL_CODEC, RunicSpell[]::new);
    private static AssetStore<String, RunicSpell, IndexedLookupTableAssetMap<String, RunicSpell>> ASSET_STORE;
    private String spellName;
    private RuneElementType elementType;
    private int castCost;
    private float castDelay;
    private float recastDelay;
    private boolean constantCast;
    private String id;
    private AssetExtraInfo.Data data;
    private boolean casted;
    private float lifeTime;

    public RunicSpell() {
    }

    public RunicSpell(String id) {
        this.id = id;
    }

    public RunicSpell(String spellName, RuneElementType elementType, int castCost, float castDelay, float recastDelay, boolean constantCast, float lifeTime) {
        this.spellName = spellName;
        this.elementType = elementType;
        this.castCost = castCost;
        this.castDelay = castDelay;
        this.recastDelay = recastDelay;
        this.constantCast = constantCast;
        this.lifeTime = lifeTime;
    }

    public static AssetStore<String, RunicSpell, IndexedLookupTableAssetMap<String, RunicSpell>> getAssetStore() {
        if (ASSET_STORE == null) {
            ASSET_STORE = AssetRegistry.getAssetStore(RunicSpell.class);
        }
        return ASSET_STORE;
    }

    public static DefaultAssetMap<String, RunicSpell> getAssetMap() {
        return getAssetStore().getAssetMap();
    }

    public boolean hasCasted() {
        return casted;
    }

    public void tick(Ref<EntityStore> ref, float tick) {
        if (tick <= 0.1F)
            this.casted = false;
        float castDelay = 0.25F;
        if (tick <= castDelay && !this.casted) {
            if (this.performanceToCast(ref, tick))
                this.casted = this.castSpell(ref);

        } else
            this.casted = true;
    }

    public boolean castSpell(Ref<EntityStore> ref) {
        return false;
    }

    /**
     * Contains the logic for defining the actions the player is to take in order to 'cast' the spell.
     *
     * @param ref
     * @param tick
     * @return
     */
    public boolean performanceToCast(Ref<EntityStore> ref, float tick) {
        return false;
    }

    public boolean shouldCast(Store<EntityStore> store, Ref<EntityStore> ref) {
        return false;
    }

    @Override
    public String getId() {
        return id;
    }

    public RuneElementType getElementType() {
        return elementType;
    }

    public void setElementType(RuneElementType elementType) {
        this.elementType = elementType;
    }

    public float getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(float lifeTime) {
        this.lifeTime = lifeTime;
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

    public String getSpellName() {
        return spellName;
    }

    public void setSpellName(String spellName) {
        this.spellName = spellName;
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


    public enum ResultStatus {
        SUCCESS(""),
        FAILURE(""),
        PASS(""),
        TICK("");

        private final String description;

        ResultStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

    }
}
