package dev.docvin.legendofelements.blocks.standingrunes.breeze;

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
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktick.BlockTickStrategy;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StandingRuneBreezeSystem extends EntityTickingSystem<ChunkStore> {

    private static final Query<ChunkStore> BLOCK_QUERY = Query.and(StandingRuneBreezeComponent.getComponentType());
    private static final Query<ChunkStore> CHUNK_QUERY = Query.and(BlockSection.getComponentType(), ChunkSection.getComponentType());
    private static final Query<EntityStore> ENTITY_STORE_QUERY = Query.and(TransformComponent.getComponentType(), Velocity.getComponentType(), BoundingBox.getComponentType());
    private double xSpeed = 0;
    private double ySpeed = 0;
    private double zSpeed = 0;


    /**
     * This tick method runs for each entity in the collision area stored in {@link StandingRuneBreezeComponent} of the current block ticking block.
     *
     * @param dt                  delta, the time in between frames
     * @param index               the index of the current entity
     * @param refEntity           memory address of the current entity
     * @param commandBufferEntity ComponentAccessor for EntityStore
     * @param blockRef            memory address of the current ticking block
     * @param commandBufferChunk  ComponentAccessor for ChunkStore
     */
    private void tick(float dt, int index, @Nonnull Ref<EntityStore> refEntity, @Nonnull CommandBuffer<EntityStore> commandBufferEntity, @Nonnull Ref<ChunkStore> blockRef, @Nonnull CommandBuffer<ChunkStore> commandBufferChunk) {
        StandingRuneBreezeComponent standingRuneBreezeComponent = commandBufferChunk.getComponent(blockRef, StandingRuneBreezeComponent.getComponentType());
        assert standingRuneBreezeComponent != null;

        Box area = standingRuneBreezeComponent.getCollisionArea();

        TransformComponent transformComponent = commandBufferEntity.getComponent(refEntity, TransformComponent.getComponentType());
        assert transformComponent != null;

        Vector3d point = transformComponent.getPosition();
        if (area.containsPosition(point)) {
            Velocity velocity = commandBufferEntity.getComponent(refEntity, Velocity.getComponentType());
            assert velocity != null;

            Vector3d speed = standingRuneBreezeComponent.getVelocity().clone().scale(standingRuneBreezeComponent.getModifier());

            double x = (speed.x - velocity.getClientVelocity().x);
            double y = (speed.y - velocity.getClientVelocity().y);
            double z = (speed.z - velocity.getClientVelocity().z);

            Player player = commandBufferEntity.getComponent(refEntity, Player.getComponentType());

            EffectControllerComponent controllerComponent = commandBufferEntity.getComponent(refEntity, EffectControllerComponent.getComponentType());


            if (player != null && controllerComponent != null) {
                assert player.getWorld() != null;

                xSpeed = controlSpeed(xSpeed, (float) speed.x, dt);
                ySpeed = controlSpeed(ySpeed, (float) speed.y, dt);
                zSpeed = controlSpeed(zSpeed, (float) speed.z, dt);

                Vector3d s = new Vector3d(xSpeed, ySpeed, zSpeed);
                velocity.addInstruction(s, null, ChangeVelocityType.Add);
                player.sendMessage(Message.raw("x " + speed + " " + xSpeed));
            }
        }
    }

    private double controlSpeed(double speed, float mod, float dt) {
        if (speed >= 0 && speed < mod) {
            speed += 5 * dt;
        } else {
            speed -= 4 * dt;
            if (speed <= 0)
                speed = 0;
        }
        return speed;
    }

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<ChunkStore> archetypeChunk, @Nonnull Store<ChunkStore> storeChunk, @Nonnull CommandBuffer<ChunkStore> commandBufferChunk) {
        BlockSection blocks = archetypeChunk.getComponent(index, BlockSection.getComponentType());

        assert blocks != null;
        if (blocks.getTickingBlocksCountCopy() != 0) {
            ChunkSection section = archetypeChunk.getComponent(index, ChunkSection.getComponentType());
            assert section != null;

            WorldChunk worldChunk = commandBufferChunk.getComponent(section.getChunkColumnReference(), WorldChunk.getComponentType());
            assert worldChunk != null;
            assert worldChunk.getEntityChunk() != null;

            World world = worldChunk.getWorld();

            BlockComponentChunk blockComponentChunk = commandBufferChunk.getComponent(section.getChunkColumnReference(), BlockComponentChunk.getComponentType());
            assert blockComponentChunk != null;

            blocks.forEachTicking(blockComponentChunk, commandBufferChunk, section.getY(), (blockComponentChunk1, commandBuffer1, localX, localY, localZ, blockId) -> {
                Ref<ChunkStore> blockRef = blockComponentChunk.getEntityReference(ChunkUtil.indexBlockInColumn(localX, localY, localZ));
                if (blockRef == null)
                    return BlockTickStrategy.IGNORED;
                else if (!BLOCK_QUERY.test(commandBuffer1.getArchetype(blockRef)))
                    return BlockTickStrategy.IGNORED;
                else {
                    Store<EntityStore> store = world.getEntityStore().getStore();

                    store.forEachChunk(ENTITY_STORE_QUERY, (chunk, buffer) -> {
                        for (int i = 0; i < chunk.size(); i++)
                            this.tick(dt, i, chunk.getReferenceTo(i), buffer, blockRef, commandBuffer1);
                    });

//                        store.forEachChunk(ENTITY_STORE_QUERY, (chunk, buffer) -> {
//                            for (int i = 0; i < chunk.size(); i++) {
//                                Ref<EntityStore> ref = chunk.getReferenceTo(i);
//                                TransformComponent transformComponent = store.getComponent(ref, TransformComponent.getComponentType());
//                                assert transformComponent != null;
//                                Vector3d pos = transformComponent.getPosition();
//                                if (pos.distanceSquaredTo(new Vector3d(globalX, localY, globalZ)) <= 25) {
//                                    BoundingBox boundingBox = store.getComponent(ref, BoundingBox.getComponentType());
//                                    assert boundingBox != null;
//                                    Box box = boundingBox.getBoundingBox().clone();
//
//                                    box.min.add(pos);
//                                    box.max.add(pos);
//                                    if (box.isIntersecting(area)) {
//                                        buffer.run(store1 -> {
//                                            Velocity velocity = store1.getComponent(ref, Velocity.getComponentType());
//                                            assert velocity != null;
//                                            velocity.addInstruction(vel, null, ChangeVelocityType.Add);
//                                        });
//                                    }
//                                }
//                            }
//                        });
                    return BlockTickStrategy.CONTINUE;
                }

            });
        }
    }

    @Nullable
    @Override
    public Query<ChunkStore> getQuery() {
        return CHUNK_QUERY;
    }
}
