package dev.docvin.legendofelements.registry;

import dev.docvin.legendofelements.blocks.standingrunes.systems.StandingRuneBreezeSystem;
import dev.docvin.legendofelements.blocks.standingrunes.systems.ref.StandingRuneBreezeInit;
import dev.docvin.legendofelements.entity.velocity.systems.AccelerateSystem;
import dev.docvin.legendofelements.rune.systems.RuneManaRegenSystem;
import dev.docvin.legendofelements.rune.systems.RuneSpellCastingSystem;

public class AllSystems implements AllRegistries {

    public static void register() {
        AllRegistries.getPlugin().getEntityStoreRegistry().registerSystem(new RuneManaRegenSystem());
        AllRegistries.getPlugin().getEntityStoreRegistry().registerSystem(new RuneSpellCastingSystem());
        AllRegistries.getPlugin().getEntityStoreRegistry().registerSystem(new AccelerateSystem());

        AllRegistries.getPlugin().getChunkStoreRegistry().registerSystem(new StandingRuneBreezeInit());
        AllRegistries.getPlugin().getChunkStoreRegistry().registerSystem(new StandingRuneBreezeSystem());
    }
}
