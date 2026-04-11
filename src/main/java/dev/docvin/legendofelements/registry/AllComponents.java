package dev.docvin.legendofelements.registry;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.blocks.BlockAnimtionComponent;
import dev.docvin.legendofelements.blocks.standingrunes.breeze.StandingRuneBreezeComponent;
import dev.docvin.legendofelements.entity.velocity.components.AccelerateComponent;
import dev.docvin.legendofelements.rune.components.KnownRuneSpellsComponent;
import dev.docvin.legendofelements.rune.components.RuneManaComponent;
import dev.docvin.legendofelements.rune.components.RuneManaRegenComponent;

public class AllComponents implements AllRegistries {

    public static void register() {
        ComponentType<EntityStore, RuneManaRegenComponent> magicRegenComponentType = AllRegistries.getEntityStoreRegistry().registerComponent(RuneManaRegenComponent.class, "Elemental_Magic_Regen", RuneManaRegenComponent.CODEC);
        RuneManaRegenComponent.setComponentType(magicRegenComponentType);

        ComponentType<EntityStore, RuneManaComponent> magicComponentType = AllRegistries.getEntityStoreRegistry().registerComponent(RuneManaComponent.class, "Elemental_Magic", RuneManaComponent.CODEC);
        RuneManaComponent.setComponentType(magicComponentType);

        ComponentType<EntityStore, KnownRuneSpellsComponent> runeKnownSpellsComponentType = AllRegistries.getEntityStoreRegistry().registerComponent(KnownRuneSpellsComponent.class, "Rune_Known_Spells", KnownRuneSpellsComponent.CODEC);
        KnownRuneSpellsComponent.setComponentType(runeKnownSpellsComponentType);

        ComponentType<EntityStore, AccelerateComponent> modifyVelocityComponentType = AllRegistries.getEntityStoreRegistry().registerComponent(AccelerateComponent.class, AccelerateComponent::new);
        AccelerateComponent.setComponentType(modifyVelocityComponentType);

        ComponentType<ChunkStore, StandingRuneBreezeComponent> breezeStandingRuneComponentType = AllRegistries.getChunkStoreRegistry().registerComponent(StandingRuneBreezeComponent.class, "Standing_Rune_Breeze", StandingRuneBreezeComponent.CODEC);
        StandingRuneBreezeComponent.setComponentType(breezeStandingRuneComponentType);
        
        ComponentType<ChunkStore, BlockAnimtionComponent> blockAnimationComponentType = AllRegistries.getChunkStoreRegistry().registerComponent(BlockAnimtionComponent.class, "Block_Animation", BlockAnimtionComponent.CODEC);
        BlockAnimtionComponent.setComponentType(blockAnimationComponentType);
    }
}
