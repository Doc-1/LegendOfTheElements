package dev.docvin.legendofelements.registry;

import dev.docvin.legendofelements.blocks.puzzle.sliding.SlidingBlockInit;
import dev.docvin.legendofelements.blocks.puzzle.sliding.SlidingBlockSystem;
import dev.docvin.legendofelements.blocks.standingrunes.breeze.StandingRuneBreezeInit;
import dev.docvin.legendofelements.blocks.standingrunes.breeze.StandingRuneBreezeSystem;
import dev.docvin.legendofelements.entity.velocity.systems.AccelerateSystem;
import dev.docvin.legendofelements.rune.systems.RuneManaRegenSystem;
import dev.docvin.legendofelements.rune.systems.RuneSpellCastingSystem;

public class AllSystems implements AllRegistries {

    public static void register() {
        AllRegistries.getPlugin().getEntityStoreRegistry().registerSystem(new RuneManaRegenSystem());
        AllRegistries.getPlugin().getEntityStoreRegistry().registerSystem(new RuneSpellCastingSystem());
        AllRegistries.getPlugin().getEntityStoreRegistry().registerSystem(new AccelerateSystem());

        AllRegistries.getPlugin().getChunkStoreRegistry().registerSystem(new SlidingBlockInit());
        AllRegistries.getPlugin().getChunkStoreRegistry().registerSystem(new SlidingBlockSystem());


        AllRegistries.getPlugin().getChunkStoreRegistry().registerSystem(new StandingRuneBreezeInit());
        AllRegistries.getPlugin().getChunkStoreRegistry().registerSystem(new StandingRuneBreezeSystem());
    }
}
