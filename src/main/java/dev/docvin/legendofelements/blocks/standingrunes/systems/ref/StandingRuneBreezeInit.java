package dev.docvin.legendofelements.blocks.standingrunes.systems.ref;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.math.shape.Box;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.RotationTuple;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import dev.docvin.legendofelements.blocks.standingrunes.components.StandingRuneBreezeComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StandingRuneBreezeInit extends RefSystem<ChunkStore> {

    private static final Query<ChunkStore> QUERY = Query.and(BlockModule.BlockStateInfo.getComponentType(), StandingRuneBreezeComponent.getComponentType());

    @Override
    public void onEntityAdded(@Nonnull Ref<ChunkStore> ref, @Nonnull AddReason addReason, @Nonnull Store<ChunkStore> store, @Nonnull CommandBuffer<ChunkStore> commandBuffer) {
        BlockModule.BlockStateInfo info = commandBuffer.getComponent(ref, BlockModule.BlockStateInfo.getComponentType());
        assert info != null;

        StandingRuneBreezeComponent generator = commandBuffer.getComponent(ref, StandingRuneBreezeComponent.getComponentType());
        assert generator != null;

        int localX = ChunkUtil.xFromBlockInColumn(info.getIndex());
        int localY = ChunkUtil.yFromBlockInColumn(info.getIndex());
        int localZ = ChunkUtil.zFromBlockInColumn(info.getIndex());

        WorldChunk worldChunk = commandBuffer.getComponent(info.getChunkRef(), WorldChunk.getComponentType());
        assert worldChunk != null;

        Vector3d min = new Vector3d(localX + (worldChunk.getX() * 32), localY, localZ + (worldChunk.getZ()) * 32);
        int rotationIndex = worldChunk.getWorld().getBlockRotationIndex((int) min.x, (int) min.y, (int) min.z);
        RotationTuple rotationTuple = RotationTuple.get(rotationIndex);
        Vector3d velocity = new Vector3d((float) rotationTuple.yaw().getRadians(), (float) rotationTuple.pitch().getRadians()).clipToZero(0.01);
        generator.setVelocity(velocity.clone().scale(-1));
        generator.setDistance(5);
        Box area = Box.cube(min, 1);
        Vector3d distance = velocity.clone().normalize().scale(5);

        switch (rotationIndex) {
            case 0, 1 -> area.max.subtract(distance);
            case 2, 3 -> area.min.subtract(distance);
        }


        generator.setCollisionArea(area);
        generator.setModifier(5F);
        worldChunk.setTicking(localX, localY, localZ, true);
    }

    @Override
    public void onEntityRemove(@Nonnull Ref<ChunkStore> ref, @Nonnull RemoveReason removeReason, @Nonnull Store<ChunkStore> store, @Nonnull CommandBuffer<ChunkStore> commandBuffer) {

    }

    @Nullable
    @Override
    public Query<ChunkStore> getQuery() {
        return StandingRuneBreezeInit.QUERY;
    }
}
