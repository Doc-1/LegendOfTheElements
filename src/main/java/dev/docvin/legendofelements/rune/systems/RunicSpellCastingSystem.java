package dev.docvin.legendofelements.rune.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.rune.assets.RunicSpell;
import dev.docvin.legendofelements.rune.components.KnownRuneSpellsComponent;
import dev.docvin.legendofelements.rune.components.RunicSystemComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * System for handling the runic spell the player is trying to cast.
 */
public class RunicSpellCastingSystem extends EntityTickingSystem<EntityStore> {
    @Nonnull
    private static final Query<EntityStore> QUERY = Query.and(RunicSystemComponent.getComponentType(), KnownRuneSpellsComponent.getComponentType());


    @Override
    public void tick(float delta, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        Player player = store.getComponent(ref, Player.getComponentType());
        assert player != null;
        KnownRuneSpellsComponent knownSpellsComponent = ref.getStore().getComponent(ref, KnownRuneSpellsComponent.getComponentType());
        assert knownSpellsComponent != null;

        RunicSystemComponent runicSystemComponent = ref.getStore().getComponent(ref, RunicSystemComponent.getComponentType());
        assert runicSystemComponent != null;

        //for loop of all known runic spells
        RunicSpell runicSpell = RunicSpell.getAssetMap().getAsset("Updraft");
        if (runicSpell != null) {

            RunicSpell.ResultStatus resultStatus = runicSpell.tick(runicSystemComponent.getTick(), ref);
            if (resultStatus.equals(RunicSpell.ResultStatus.TICK))
                runicSystemComponent.tick(delta);

        }
    }


    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return QUERY;
    }
}
