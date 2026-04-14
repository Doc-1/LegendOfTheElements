package dev.docvin.legendofelements.blocks.entity;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktick.BlockTickStrategy;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.section.BlockSection;
import com.hypixel.hytale.server.core.universe.world.chunk.section.ChunkSection;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

import javax.annotation.Nonnull;

public abstract class BlockEntityTickingSystem extends EntityTickingSystem<ChunkStore> {

    private static final Query<ChunkStore> CHUNK_QUERY = Query.and(BlockSection.getComponentType(), ChunkSection.getComponentType());

    @Override
    public final void tick(float dt, int index, @Nonnull ArchetypeChunk<ChunkStore> archetypeChunk, @Nonnull Store<ChunkStore> store, @Nonnull CommandBuffer<ChunkStore> commandBufferChunk) {
        BlockSection blocks = archetypeChunk.getComponent(index, BlockSection.getComponentType());

        assert blocks != null;
        if (blocks.getTickingBlocksCountCopy() != 0) {
            ChunkSection section = archetypeChunk.getComponent(index, ChunkSection.getComponentType());
            assert section != null;

            WorldChunk worldChunk = commandBufferChunk.getComponent(section.getChunkColumnReference(), WorldChunk.getComponentType());
            assert worldChunk != null;
            assert worldChunk.getEntityChunk() != null;

            BlockComponentChunk blockComponentChunk = commandBufferChunk.getComponent(section.getChunkColumnReference(), BlockComponentChunk.getComponentType());
            assert blockComponentChunk != null;
            blocks.forEachTicking(blockComponentChunk, commandBufferChunk, section.getY(), (blockComponentChunk1, commandBuffer1, localX, localY, localZ, blockId) -> {
                Ref<ChunkStore> blockRef = blockComponentChunk1.getEntityReference(ChunkUtil.indexBlockInColumn(localX, localY, localZ));
                Archetype<ChunkStore> archetype;
                if (blockRef == null)
                    return BlockTickStrategy.IGNORED;
                else
                    archetype = commandBuffer1.getArchetype(blockRef);

                int globalX = worldChunk.getX() * 32 + localX;
                int globalY = worldChunk.getZ() * 32 + localZ;
                if (this.getBlockQuery().test(archetype) && this.validBlock(blockRef, commandBuffer1, worldChunk)) {
                    this.blockTick(dt, blockId, blockRef, commandBuffer1, worldChunk, new Vector3i(globalX, localY, globalY));
                    return BlockTickStrategy.CONTINUE;
                } else
                    return BlockTickStrategy.IGNORED;
            });
        }
    }

    /**
     * This method will run every tick for any block entity that matches the {@link Query} provided by {@link #getBlockQuery()} and if {@link #validBlock(Ref, CommandBuffer, WorldChunk)}
     * returns a true.
     *
     * @param dt            delta, the time delay between frames
     * @param index         id of the block used to get the ref
     * @param ref           reference of the block
     * @param commandBuffer command buffer of the block
     * @param targetBlock   global location of the block
     */
    public abstract void blockTick(float dt, int index, @Nonnull Ref<ChunkStore> ref, @Nonnull CommandBuffer<ChunkStore> commandBuffer, @Nonnull WorldChunk worldChunk, @Nonnull Vector3i targetBlock);


    /**
     * Allows for a more defined check if the component check provided by {@link #getBlockQuery()} is not enough to handle only the desired block entities.
     * If the return remains true then the system will only use {@link #getBlockQuery()} to check the block entity.
     *
     * @param ref           reference of the block
     * @param commandBuffer command buffer of the block
     * @return true if the block should be handled by this system.
     */
    public boolean validBlock(@Nonnull Ref<ChunkStore> ref, @Nonnull CommandBuffer<ChunkStore> commandBuffer, @Nonnull WorldChunk worldChunk) {
        return true;
    }

    /**
     * Provides the query that will be used to check the ticking block entity to see if it should be handled by this system. For example,
     * if you set the {@link Query} to "Query.and(CoopBlock.getComponentType());" the system will only handle blocks with that component assigned to it.
     *
     * @return the {@link Query} that defines the required components the ticking block entity must contain.
     */
    public abstract Query<ChunkStore> getBlockQuery();

    @Override
    public final Query<ChunkStore> getQuery() {
        return CHUNK_QUERY;
    }
}

