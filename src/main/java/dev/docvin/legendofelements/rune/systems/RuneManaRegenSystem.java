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
import dev.docvin.legendofelements.rune.components.RunicSystemComponent;

import javax.annotation.Nonnull;

/**
 * System that handles player entities with the {@link RuneManaRegenComponent}
 */
public class RuneManaRegenSystem extends EntityTickingSystem<EntityStore> {


    private static final Query<EntityStore> QUERY = Query.and(RuneManaRegenComponent.getComponentType(), RunicSystemComponent.getComponentType());

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        RuneManaRegenComponent magicRegenComponent = archetypeChunk.getComponent(index, RuneManaRegenComponent.getComponentType());
        RunicSystemComponent magicComponent = archetypeChunk.getComponent(index, RunicSystemComponent.getComponentType());

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