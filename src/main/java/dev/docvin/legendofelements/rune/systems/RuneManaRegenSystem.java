package dev.docvin.legendofelements.rune.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.rune.components.RuneManaRegenComponent;
import dev.docvin.legendofelements.rune.components.RunicCastingManagmentComponent;

import javax.annotation.Nonnull;

/**
 * System that handles player entities with the {@link RuneManaRegenComponent}. This component only needs to be set to the player
 * when the player needs to regen mana. If you attempt to get {@link RuneManaRegenSystem} and it is null then that means the
 * player's mana is at max.
 */
//todo make it be used in casting of spells.
public class RuneManaRegenSystem extends EntityTickingSystem<EntityStore> {


    private static final Query<EntityStore> QUERY = Query.and(RuneManaRegenComponent.getComponentType(), RunicCastingManagmentComponent.getComponentType());

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        RuneManaRegenComponent magicRegenComponent = archetypeChunk.getComponent(index, RuneManaRegenComponent.getComponentType());
        RunicCastingManagmentComponent magicComponent = archetypeChunk.getComponent(index, RunicCastingManagmentComponent.getComponentType());

        assert magicRegenComponent != null;
        assert magicComponent != null;

        int currentMagic = magicComponent.getCurrentMagic();
        int maxMagic = magicComponent.getMaxMagic();
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        Player player = store.getComponent(ref, Player.getComponentType());
        assert player != null;
        if (currentMagic < maxMagic) {
            magicRegenComponent.addElapsedTime(dt);
            if (magicRegenComponent.getElapsedTime() >= magicRegenComponent.getRegenDelay()) {
                magicComponent.regenMagic(magicRegenComponent.getMagicRegen());
                magicRegenComponent.setElapsedTime(0);
            }
            player.sendMessage(Message.raw(magicComponent.getCurrentMagic() + " "));
        } else {
            commandBuffer.tryRemoveComponent(ref, RuneManaRegenComponent.getComponentType());
            player.sendMessage(Message.raw("Finished regenerating"));
        }
    }

    @Override
    public Query<EntityStore> getQuery() {
        return QUERY;
    }
}