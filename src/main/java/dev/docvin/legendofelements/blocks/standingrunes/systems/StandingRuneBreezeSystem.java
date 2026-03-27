package dev.docvin.legendofelements.blocks.standingrunes.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.shape.Box;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.ChangeVelocityType;
import com.hypixel.hytale.server.core.asset.type.blocktick.BlockTickStrategy;
import com.hypixel.hytale.server.core.modules.entity.component.BoundingBox;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.section.BlockSection;
import com.hypixel.hytale.server.core.universe.world.chunk.section.ChunkSection;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.blocks.standingrunes.components.StandingRuneBreezeComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StandingRuneBreezeSystem extends EntityTickingSystem<ChunkStore> {

    private static final Query<ChunkStore> QUERY = Query.and(BlockSection.getComponentType(), ChunkSection.getComponentType());
    private static final Query<EntityStore> ENTITY_STORE_QUERY = Query.and(TransformComponent.getComponentType(), Velocity.getComponentType(), BoundingBox.getComponentType());

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<ChunkStore> archetypeChunk, @Nonnull Store<ChunkStore> storeChunk, @Nonnull CommandBuffer<ChunkStore> commandBufferChunk) {
        BlockSection blocks = archetypeChunk.getComponent(index, BlockSection.getComponentType());

        assert blocks != null;
        if (blocks.getTickingBlocksCountCopy() != 0) {
            ChunkSection section = archetypeChunk.getComponent(index, ChunkSection.getComponentType());
            assert section != null;

            BlockComponentChunk blockComponentChunk = commandBufferChunk.getComponent(section.getChunkColumnReference(), BlockComponentChunk.getComponentType());
            assert blockComponentChunk != null;
            WorldChunk worldChunk = commandBufferChunk.getComponent(section.getChunkColumnReference(), WorldChunk.getComponentType());
            assert worldChunk != null;
            assert worldChunk.getEntityChunk() != null;

            World world = worldChunk.getWorld();

            blocks.forEachTicking(blockComponentChunk, commandBufferChunk, section.getY(), (blockComponentChunk1, commandBuffer1, localX, localY, localZ, blockId) -> {
                Ref<ChunkStore> blockRef = blockComponentChunk.getEntityReference(ChunkUtil.indexBlockInColumn(localX, localY, localZ));

                if (blockRef == null) {
                    return BlockTickStrategy.IGNORED;
                } else {
                    StandingRuneBreezeComponent standingRuneBreezeComponent = commandBuffer1.getComponent(blockRef, StandingRuneBreezeComponent.getComponentType());
                    if (standingRuneBreezeComponent != null) {

                        Store<EntityStore> store = world.getEntityStore().getStore();
                        int globalX = localX + (worldChunk.getX() * 32);
                        int globalZ = localZ + (worldChunk.getZ() * 32);

                        float modifier = standingRuneBreezeComponent.getModifier();

                        Vector3d vel = standingRuneBreezeComponent.getVelocity().scale(modifier);
                        Box area = standingRuneBreezeComponent.getCollisionArea();

                        store.forEachChunk(ENTITY_STORE_QUERY, (chunk, buffer) -> {
                            for (int i = 0; i < chunk.size(); i++) {
                                Ref<EntityStore> ref = chunk.getReferenceTo(i);
                                TransformComponent transformComponent = store.getComponent(ref, TransformComponent.getComponentType());
                                assert transformComponent != null;
                                Vector3d pos = transformComponent.getPosition();
                                if (pos.distanceSquaredTo(new Vector3d(globalX, localY, globalZ)) <= 25) {
                                    BoundingBox boundingBox = store.getComponent(ref, BoundingBox.getComponentType());
                                    assert boundingBox != null;
                                    Box box = boundingBox.getBoundingBox().clone();

                                    box.min.add(pos);
                                    box.max.add(pos);
                                    if (box.isIntersecting(area)) {
                                        buffer.run(store1 -> {
                                            Velocity velocity = store1.getComponent(ref, Velocity.getComponentType());
                                            assert velocity != null;
                                            velocity.addInstruction(vel, null, ChangeVelocityType.Add);
                                        });
                                    }
                                }
                            }
                        });
                        return BlockTickStrategy.CONTINUE;
                    } else {
                        return BlockTickStrategy.IGNORED;
                    }
                }
            });
        }
    }

    @Nullable
    @Override
    public Query<ChunkStore> getQuery() {
        return QUERY;
    }
}
