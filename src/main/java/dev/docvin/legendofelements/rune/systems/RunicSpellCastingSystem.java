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
import dev.docvin.legendofelements.rune.components.RunicCastingManagmentComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * System for handling the runic spell the player is trying to cast.
 */
public class RunicSpellCastingSystem extends EntityTickingSystem<EntityStore> {
    @Nonnull
    private static final Query<EntityStore> QUERY = Query.and(RunicCastingManagmentComponent.getComponentType(), KnownRuneSpellsComponent.getComponentType());


    @Override
    public void tick(float delta, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        Player player = store.getComponent(ref, Player.getComponentType());
        assert player != null;
        KnownRuneSpellsComponent knownSpellsComponent = ref.getStore().getComponent(ref, KnownRuneSpellsComponent.getComponentType());
        assert knownSpellsComponent != null;

        RunicCastingManagmentComponent runicCastingManagmentComponent = ref.getStore().getComponent(ref, RunicCastingManagmentComponent.getComponentType());
        assert runicCastingManagmentComponent != null;
        int currentSpell = runicCastingManagmentComponent.getCurrentSpellCasting();
        if (currentSpell == -1) {
            int i = 0;
            for (RunicSpell.Data runicSpell : knownSpellsComponent.getKnownSpells()) {
                tryCast(delta, ref, RunicSpell.getAssetMap().getAsset(runicSpell.getSpellName()), runicCastingManagmentComponent, i);
                i++;
            }
        } else
            tryCast(delta, ref, RunicSpell.getAssetMap().getAsset(knownSpellsComponent.getKnownSpells()[currentSpell].getSpellName()), runicCastingManagmentComponent, currentSpell);
    }


    /**
     * Tests to see if the runicSpell has been activated and then succeeds in completing casting the spell.
     *
     * @param delta
     * @param ref
     * @param runicSpell
     * @param runicCastingManagmentComponent
     * @param index
     */
    private void tryCast(float delta, Ref<EntityStore> ref, RunicSpell runicSpell, RunicCastingManagmentComponent runicCastingManagmentComponent, int index) {
        if (runicSpell != null) {
            if (!runicCastingManagmentComponent.isCasting()) {
                runicCastingManagmentComponent.resetTick();
                boolean d = runicSpell.shouldStartTicking(ref.getStore(), ref);
                runicCastingManagmentComponent.setCasting(d);
            }

            if (runicCastingManagmentComponent.isCasting()) {
                runicCastingManagmentComponent.setCurrentSpellCasting(index);
                runicCastingManagmentComponent.tick(delta);

                runicSpell.tick(ref, runicCastingManagmentComponent.getTick());
                if (runicSpell.hasCasted()) {
                    runicCastingManagmentComponent.setCurrentSpellCasting(-1);
                    runicCastingManagmentComponent.setCasting(false);
                }
            }
        }
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return QUERY;
    }
}
