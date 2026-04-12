package dev.docvin.legendofelements.blocks.puzzle.sliding;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import dev.docvin.legendofelements.blocks.BlockAnimtionComponent;
import dev.docvin.legendofelements.blocks.entity.BlockEntityTickingSystem;

import javax.annotation.Nonnull;

public class SlidingBlockSystem extends BlockEntityTickingSystem {

    private final Query<ChunkStore> BLOCK_QUERY = Query.and(BlockAnimtionComponent.getComponentType());

    @Override
    public void blockTick(float dt, int index, @Nonnull Ref<ChunkStore> ref, @Nonnull CommandBuffer<ChunkStore> commandBuffer, @Nonnull WorldChunk worldChunk, @Nonnull Vector3i targetBlock) {

        World world = worldChunk.getWorld();
        world.execute(() -> {
            world.breakBlock(targetBlock.x, targetBlock.y, targetBlock.z, 0);

        });
    }

    @Override
    public boolean validBlock(@Nonnull Ref<ChunkStore> ref, @Nonnull CommandBuffer<ChunkStore> commandBuffer, @Nonnull WorldChunk worldChunk) {
        BlockAnimtionComponent blockAnimtionComponent = commandBuffer.getComponent(ref, BlockAnimtionComponent.getComponentType());
        assert blockAnimtionComponent != null;

        return blockAnimtionComponent.getAnimationID().equals("Sliding_Animation");
    }

    @Override
    public Query<ChunkStore> getBlockQuery() {
        return BLOCK_QUERY;
    }
}
