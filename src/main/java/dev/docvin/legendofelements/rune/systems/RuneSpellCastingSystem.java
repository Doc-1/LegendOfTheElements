package dev.docvin.legendofelements.rune.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.server.core.entity.InteractionChain;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.InteractionManager;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.modules.interaction.InteractionModule;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.RootInteraction;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.rune.assets.RuneSpellsAsset;
import dev.docvin.legendofelements.rune.components.KnownRuneSpellsComponent;
import dev.docvin.legendofelements.rune.components.RuneManaComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * System for handle the runic spell the player is trying to cast.
 */
public class RuneSpellCastingSystem extends EntityTickingSystem<EntityStore> {
    @Nonnull
    private static final Query<EntityStore> QUERY = Query.and(RuneManaComponent.getComponentType(), KnownRuneSpellsComponent.getComponentType(), InteractionModule.get().getInteractionManagerComponent());


    @Override
    public void tick(float var1, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        Player player = store.getComponent(ref, Player.getComponentType());
        assert player != null;
        KnownRuneSpellsComponent knownSpellsComponent = ref.getStore().getComponent(ref, KnownRuneSpellsComponent.getComponentType());
        assert knownSpellsComponent != null;

        //todo design a spell selection system.
        RuneSpellsAsset runeSpell = RuneSpellsAsset.getAssetMap().getAssetMap().get("Updraft");
        String rootID = runeSpell.getInteractions().get(InteractionType.Ability2);
        RootInteraction interaction = RootInteraction.getAssetMap().getAsset(rootID);

        MovementStates states = Objects.requireNonNull(ref.getStore().getComponent(ref, MovementStatesComponent.getComponentType())).getMovementStates();

        if (interaction == null)
            return;
        if (states.crouching) {
            InteractionManager interactionManager = archetypeChunk.getComponent(index, InteractionModule.get().getInteractionManagerComponent());
            assert interactionManager != null;
            InteractionContext context = InteractionContext.forInteraction(interactionManager, ref, InteractionType.Ability2, commandBuffer);
            context.setInteractionVarsGetter(runeSpell::getInteractionVars);
            InteractionChain chain = interactionManager.initChain(InteractionType.Ability2, context, interaction, true);
            interactionManager.queueExecuteChain(chain);
            //player.sendMessage(Message.raw(knownSpellsComponent.knowsRuneSpell(RuneSpellManager.get().getRuneSpell("Updraft")) + " "));
        }
    }


    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return QUERY;
    }
}
