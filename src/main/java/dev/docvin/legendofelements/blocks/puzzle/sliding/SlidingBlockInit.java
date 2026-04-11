package dev.docvin.legendofelements.blocks.puzzle.sliding;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import dev.docvin.legendofelements.blocks.BlockAnimtionComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SlidingBlockInit extends RefSystem<ChunkStore> {
    private static final Query<ChunkStore> QUERY = Query.and(BlockModule.BlockStateInfo.getComponentType(), BlockAnimtionComponent.getComponentType());

    @Override
    public void onEntityAdded(@Nonnull Ref<ChunkStore> ref, @Nonnull AddReason addReason, @Nonnull Store<ChunkStore> store, @Nonnull CommandBuffer<ChunkStore> commandBuffer) {
        BlockModule.BlockStateInfo info = commandBuffer.getComponent(ref, BlockModule.BlockStateInfo.getComponentType());
        assert info != null;
        int localX = ChunkUtil.xFromBlockInColumn(info.getIndex());
        int localY = ChunkUtil.yFromBlockInColumn(info.getIndex());
        int localZ = ChunkUtil.zFromBlockInColumn(info.getIndex());

        WorldChunk worldChunk = commandBuffer.getComponent(info.getChunkRef(), WorldChunk.getComponentType());
        assert worldChunk != null;

        worldChunk.setTicking(localX, localY, localZ, true);

        //To do check for when state changes
    }

    @Override
    public void onEntityRemove(@Nonnull Ref<ChunkStore> ref, @Nonnull RemoveReason removeReason, @Nonnull Store<ChunkStore> store, @Nonnull CommandBuffer<ChunkStore> commandBuffer) {

    }

    @Nullable
    @Override
    public Query<ChunkStore> getQuery() {
        return QUERY;
    }
}
