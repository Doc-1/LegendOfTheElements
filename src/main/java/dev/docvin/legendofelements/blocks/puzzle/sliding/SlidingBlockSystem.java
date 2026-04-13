package dev.docvin.legendofelements.blocks.puzzle.sliding;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
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
        BlockAnimtionComponent blockAnimtionComponent = commandBuffer.getComponent(ref, BlockAnimtionComponent.getComponentType());
        assert blockAnimtionComponent != null;
        World world = worldChunk.getWorld();
        BlockType blockType = world.getBlockType(targetBlock);
        assert blockType != null;
        String blockState = blockType.getStateForBlock(blockType);

        if (blockState != null && !blockState.equals(blockType.getDefaultStateKey()))
            blockAnimtionComponent.tickAnimation(dt);
        if (blockAnimtionComponent.animationCompleted()) {

            world.execute(() -> {
                world.breakBlock(targetBlock.x, targetBlock.y, targetBlock.z, 0);
                String key = blockType.getDefaultStateKey();
                if (key != null) {
                    assert blockState != null;
                    Vector3i dir = switch (blockState) {
                        case "Slide_South" -> Vector3i.SOUTH;
                        case "Slide_West" -> Vector3i.WEST;
                        case "Slide_North" -> Vector3i.NORTH;
                        case "Slide_East" -> Vector3i.EAST;
                        default -> Vector3i.ZERO;
                    };
                    dir = dir.clone().add(targetBlock);
                    world.setBlock(dir.x, dir.y, dir.z, key, 0);
                }
            });
        }
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
