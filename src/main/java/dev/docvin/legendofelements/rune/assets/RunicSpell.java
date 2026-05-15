package dev.docvin.legendofelements.rune.assets;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.assetstore.codec.AssetCodecMapCodec;
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

    public static final AssetCodecMapCodec<String, RunicSpell> CODEC = new AssetCodecMapCodec<>(Codec.STRING, (t, k) -> t.id = k, t -> t.id, (t, data) -> t.extraData = data, t -> t.extraData);
    public static final BuilderCodec<RunicSpell.Data> SPELL_DATA_CODEC = BuilderCodec.builder(RunicSpell.Data.class, RunicSpell.Data::new)
            .appendInherited(
                    new KeyedCodec<>("SpellName", Codec.STRING),
                    (c, v) -> c.spellName = v,
                    c -> c.spellName,
                    (spell, parent) -> spell.spellName = parent.spellName)
            .add()
            .appendInherited(new KeyedCodec<>("ElementType", new EnumCodec<>(RuneElementType.class).documentKey(RuneElementType.WIND, "Wind Element")), (c, v) -> c.elementType = v, c -> c.elementType,
                    (spell, parent) -> spell.elementType = parent.elementType)
            .add()
            .appendInherited(new KeyedCodec<>("CastCost", Codec.INTEGER, true), (c, v) -> c.castCost = v, c -> c.castCost,
                    (spell, parent) -> spell.castCost = parent.castCost)
            .add()
            .appendInherited(new KeyedCodec<>("CastDelay", Codec.FLOAT), (c, v) -> c.castDelay = v, c -> c.castDelay,
                    (spell, parent) -> spell.castDelay = parent.castDelay)
            .add()
            .appendInherited(new KeyedCodec<>("RecastDelay", Codec.FLOAT), (c, v) -> c.recastDelay = v, c -> c.recastDelay,
                    (spell, parent) -> spell.recastDelay = parent.recastDelay)
            .add()
            .appendInherited(new KeyedCodec<>("ConstantCast", Codec.BOOLEAN), (c, v) -> c.constantCast = v, c -> c.constantCast,
                    (spell, parent) -> spell.constantCast = parent.constantCast)
            .add()
            .appendInherited(new KeyedCodec<>("LifeTime", Codec.FLOAT), (c, v) -> c.lifeTime = v, c -> c.lifeTime,
                    (spell, parent) -> spell.lifeTime = parent.lifeTime)
            .add()
            .build();
    public static final BuilderCodec<RunicSpell> SPELL_CODEC = BuilderCodec.abstractBuilder(RunicSpell.class)
            .appendInherited(new KeyedCodec<>("SpellData", SPELL_DATA_CODEC), (c, v) -> c.spellData = v, c -> c.spellData, (c, parent) -> c.spellData = parent.spellData)
            .add()
            .build();
    @Nonnull
    public static final Codec<RunicSpell.Data[]> ARRAY_CODEC = new ArrayCodec<>(SPELL_DATA_CODEC, RunicSpell.Data[]::new);
    private static AssetStore<String, RunicSpell, IndexedLookupTableAssetMap<String, RunicSpell>> ASSET_STORE;
    private RunicSpell.Data spellData;
    private String id;
    private AssetExtraInfo.Data extraData;
    private boolean casted;

    public RunicSpell() {
    }

    public RunicSpell(String id) {
        this.id = id;
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
        if (tick <= spellData.getCastCost() && !this.casted) {
            if (this.performanceToCast(ref, tick))
                this.casted = this.castSpell(ref);

        } else
            this.casted = true;
    }

    /**
     * This defines how the spell should act when cast.
     *
     * @param ref
     * @return true spell has finished casting and ticking ends
     * <p>
     * false spell has not finished casting and ticking continues
     */
    public boolean castSpell(Ref<EntityStore> ref) {
        return false;
    }

    /**
     * This is where the logic for defining the actions the player needs to take to cast the currently ticking spell selected by
     * {@link RunicSpell#shouldStartTicking(Store, Ref)}.
     *
     * @param ref
     * @param tick
     * @return
     */
    public boolean performanceToCast(Ref<EntityStore> ref, float tick) {
        return false;
    }

    /**
     * This is where you set the logic to define when a spell should begin to tick and listening for the action defined by {@link RunicSpell#performanceToCast(Ref, float)}
     * to perform the spell.
     *
     * @param store
     * @param ref
     * @return true if this spell should begin to tick.
     */
    public boolean shouldStartTicking(Store<EntityStore> store, Ref<EntityStore> ref) {
        return false;
    }

    @Override
    public String getId() {
        return id;
    }

    public RunicSpell.Data getSpellData() {
        return this.spellData;
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

    public static class Data {
        private RuneElementType elementType;
        private int castCost;
        private float castDelay;
        private float recastDelay;
        private boolean constantCast;
        private float lifeTime;
        private String spellName;

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

        public int getCastCost() {
            return this.castCost;
        }

        public void setCastCost(int castCost) {
            this.castCost = castCost;
        }

        private void setCastCost(Integer castCost) {
            this.castCost = Math.max(0, castCost);
        }

        public RunicSpell.Data clone() {
            RunicSpell.Data clone = new RunicSpell.Data();
            clone.setCastCost(this.getCastCost());
            clone.setCastDelay(this.getCastDelay());
            clone.setConstantCast(this.isConstantCast());
            clone.setElementType(this.getElementType());
            clone.setRecastDelay(this.getRecastDelay());
            return clone;
        }
    }
}
