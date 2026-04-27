package dev.docvin.legendofelements.registry;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.chunk.blocks.components.BlockAnimtionComponent;
import dev.docvin.legendofelements.chunk.blocks.components.BlockNeighbourNotifierComponent;
import dev.docvin.legendofelements.chunk.blocks.components.LockableBlockComponent;
import dev.docvin.legendofelements.chunk.blocks.components.StandingRuneBreezeComponent;
import dev.docvin.legendofelements.chunk.components.FirstRunComponent;
import dev.docvin.legendofelements.entity.entities.components.PhysicsBlockComponent;
import dev.docvin.legendofelements.items.ItemKeyComponent;
import dev.docvin.legendofelements.rune.components.KnownRuneSpellsComponent;
import dev.docvin.legendofelements.rune.components.RuneManaComponent;
import dev.docvin.legendofelements.rune.components.RuneManaRegenComponent;

public class AllComponents implements AllRegistries {

    public static void register() {
        ComponentType<EntityStore, RuneManaRegenComponent> magicRegenComponentType = AllRegistries.getEntityStoreRegistry().registerComponent(RuneManaRegenComponent.class, "Elemental_Magic_Regen", RuneManaRegenComponent.CODEC);
        RuneManaRegenComponent.setComponentType(magicRegenComponentType);

        ComponentType<EntityStore, RuneManaComponent> magicComponentType = AllRegistries.getEntityStoreRegistry().registerComponent(RuneManaComponent.class, "Elemental_Magic", RuneManaComponent.CODEC);
        RuneManaComponent.setComponentType(magicComponentType);

        AllRegistries.registerComponent(KnownRuneSpellsComponent::new);

//        ComponentType<EntityStore, AccelerateComponent> modifyVelocityComponentType = AllRegistries.getEntityStoreRegistry().registerComponent(AccelerateComponent.class, AccelerateComponent::new);
//        AccelerateComponent.setComponentType(modifyVelocityComponentType);

        ComponentType<EntityStore, PhysicsBlockComponent> psudoBlockComponentType = AllRegistries.getEntityStoreRegistry().registerComponent(PhysicsBlockComponent.class, "Standing_Rune_Breeze", PhysicsBlockComponent.CODEC);
        PhysicsBlockComponent.setComponentType(psudoBlockComponentType);

        AllRegistries.registerComponent(ItemKeyComponent::new);
        ComponentType<ChunkStore, StandingRuneBreezeComponent> breezeStandingRuneComponentType = AllRegistries.getChunkStoreRegistry().registerComponent(StandingRuneBreezeComponent.class, "Standing_Rune_Breeze", StandingRuneBreezeComponent.CODEC);
        StandingRuneBreezeComponent.setComponentType(breezeStandingRuneComponentType);


        ComponentType<ChunkStore, FirstRunComponent> firstRunComponentType = AllRegistries.getChunkStoreRegistry().registerComponent(FirstRunComponent.class, "First_Run", FirstRunComponent.CODEC);
        FirstRunComponent.setComponentType(firstRunComponentType);

        ComponentType<ChunkStore, BlockNeighbourNotifierComponent> blockNeighbourNotifierComponentType = AllRegistries.getChunkStoreRegistry().registerComponent(BlockNeighbourNotifierComponent.class, "Block_Neighbour_Notifier", BlockNeighbourNotifierComponent.CODEC);
        BlockNeighbourNotifierComponent.setComponentType(blockNeighbourNotifierComponentType);

        ComponentType<ChunkStore, LockableBlockComponent> lockableBlockComponentType = AllRegistries.getChunkStoreRegistry().registerComponent(LockableBlockComponent.class, "Lockable_Block", LockableBlockComponent.CODEC);
        LockableBlockComponent.setComponentType(lockableBlockComponentType);

        AllRegistries.registerComponent(BlockAnimtionComponent::new);
    }
}
