package dev.docvin.legendofelements.registry;

import dev.docvin.legendofelements.block.blocks.puzzle.sliding.SlidingBlockInit;
import dev.docvin.legendofelements.block.blocks.puzzle.sliding.SlidingBlockSystem;
import dev.docvin.legendofelements.block.blocks.standingrunes.breeze.StandingRuneBreezeInit;
import dev.docvin.legendofelements.block.blocks.standingrunes.breeze.StandingRuneBreezeSystem;
import dev.docvin.legendofelements.entity.entities.EnsurePhysicsBlockComponents;
import dev.docvin.legendofelements.entity.entities.PhysicsBlockEntitySystem;
import dev.docvin.legendofelements.event.events.entity.BlockBreakNotifySystem;
import dev.docvin.legendofelements.rune.systems.RuneManaRegenSystem;
import dev.docvin.legendofelements.rune.systems.RuneSpellCastingSystem;

public class AllSystems implements AllRegistries {

    public static void register() {
        AllRegistries.getEntityStoreRegistry().registerSystem(new RuneManaRegenSystem());
        AllRegistries.getEntityStoreRegistry().registerSystem(new RuneSpellCastingSystem());
        AllRegistries.getEntityStoreRegistry().registerSystem(new PhysicsBlockEntitySystem());
        AllRegistries.getEntityStoreRegistry().registerSystem(new EnsurePhysicsBlockComponents());

        AllRegistries.getEntityStoreRegistry().registerSystem(new BlockBreakNotifySystem());

        AllRegistries.getChunkStoreRegistry().registerSystem(new SlidingBlockInit());
        AllRegistries.getChunkStoreRegistry().registerSystem(new SlidingBlockSystem());
        AllRegistries.getChunkStoreRegistry().registerSystem(new StandingRuneBreezeInit());
        AllRegistries.getChunkStoreRegistry().registerSystem(new StandingRuneBreezeSystem());

    }
}
