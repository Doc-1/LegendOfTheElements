package dev.docvin.legendofelements.registry;

import dev.docvin.legendofelements.chunk.blocks.entities.SlidingBlockInit;
import dev.docvin.legendofelements.chunk.blocks.entities.StandingRuneBreezeInit;
import dev.docvin.legendofelements.chunk.blocks.systems.SlidingBlockSystem;
import dev.docvin.legendofelements.chunk.blocks.systems.StandingRuneBreezeSystem;
import dev.docvin.legendofelements.entity.entities.holders.PhysicsBlockEntityInit;
import dev.docvin.legendofelements.entity.entities.systems.PhysicsBlockEntitySystem;
import dev.docvin.legendofelements.event.events.entity.BlockBreakNotifySystem;
import dev.docvin.legendofelements.rune.systems.RuneManaRegenSystem;
import dev.docvin.legendofelements.rune.systems.RuneSpellCastingSystem;

public class AllSystems implements AllRegistries {

    public static void register() {
        AllRegistries.getEntityStoreRegistry().registerSystem(new RuneManaRegenSystem());
        AllRegistries.getEntityStoreRegistry().registerSystem(new RuneSpellCastingSystem());
        AllRegistries.getEntityStoreRegistry().registerSystem(new PhysicsBlockEntitySystem());
        AllRegistries.getEntityStoreRegistry().registerSystem(new PhysicsBlockEntityInit());

        AllRegistries.getEntityStoreRegistry().registerSystem(new BlockBreakNotifySystem());

        AllRegistries.getChunkStoreRegistry().registerSystem(new SlidingBlockInit());
        AllRegistries.getChunkStoreRegistry().registerSystem(new SlidingBlockSystem());
        AllRegistries.getChunkStoreRegistry().registerSystem(new StandingRuneBreezeInit());
        AllRegistries.getChunkStoreRegistry().registerSystem(new StandingRuneBreezeSystem());

    }
}
