package dev.docvin.legendofelements.blocks.puzzle.sliding;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.asset.type.blocktick.BlockTickStrategy;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.section.BlockSection;
import com.hypixel.hytale.server.core.universe.world.chunk.section.ChunkSection;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import dev.docvin.legendofelements.blocks.BlockAnimtionComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SlidingBlockSystem extends EntityTickingSystem<ChunkStore> {
    @Nonnull
    private final ComponentType<ChunkStore, BlockAnimtionComponent> blockAnimtionComponentType;
    @Nonnull
    private final Archetype<ChunkStore> archetype;

    public SlidingBlockSystem(@Nonnull ComponentType<ChunkStore, BlockAnimtionComponent> blockAnimtionComponentType) {
        this.blockAnimtionComponentType = blockAnimtionComponentType;
        this.archetype = Archetype.of(blockAnimtionComponentType, WorldChunk.getComponentType());
    }

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<ChunkStore> archetypeChunk, @Nonnull Store<ChunkStore> store, @Nonnull CommandBuffer<ChunkStore> commandBufferChunk) {
        BlockSection blocks = archetypeChunk.getComponent(index, BlockSection.getComponentType());

        assert blocks != null;
        if (blocks.getTickingBlocksCountCopy() != 0) {
            ChunkSection section = archetypeChunk.getComponent(index, ChunkSection.getComponentType());
            assert section != null;

            WorldChunk chunk = commandBufferChunk.getComponent(section.getChunkColumnReference(), WorldChunk.getComponentType());
            assert chunk != null;

            BlockComponentChunk blockComponentChunk = commandBufferChunk.getComponent(section.getChunkColumnReference(), BlockComponentChunk.getComponentType());
            assert blockComponentChunk != null;

            blocks.forEachTicking(blockComponentChunk, commandBufferChunk, section.getY(), (blockComponentChunk1, commandBuffer1, localX, localY, localZ, blockId) -> {
                Ref<ChunkStore> blockRef = blockComponentChunk1.getEntityReference(blockId);

                if (blockRef == null)
                    return BlockTickStrategy.IGNORED;
                BlockType current = chunk.getBlockType(localX, localY, localZ);

                // System.out.println(current);
                //String currentState = current.getStateForBlock(current);
                return BlockTickStrategy.CONTINUE;
            });
        }
    }

    @Nullable
    @Override
    public Query<ChunkStore> getQuery() {
        return this.archetype;
    }
}
