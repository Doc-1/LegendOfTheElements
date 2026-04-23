package dev.docvin.legendofelements.block;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.math.shape.Box;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.BoundingBox;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.PersistentModel;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.entity.entities.PhysicsBlockComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpawnFallingBlockInteraction extends SimpleBlockInteraction {
    public static final BuilderCodec<SpawnFallingBlockInteraction> CODEC = BuilderCodec.builder(
            SpawnFallingBlockInteraction.class, SpawnFallingBlockInteraction::new, SimpleBlockInteraction.CODEC
    ).build();

    /**
     * Default box to be used to create 1^3 bounding box the size of an in game block.
     */
    public static final Box HORIZONTALLY_CENTERED = Box.horizontallyCentered(0.5, 0.5, 0.5);

    public static Holder<EntityStore> getFallingBlockHolder(@Nonnull Vector3i targetBlock) {
        Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();
        Vector3d targetPosition = targetBlock.toVector3d();
        targetPosition.add(0.5, 1, 0.5);
        Vector3f rotation = new Vector3f(0F, (float) Math.PI, 0F);

        holder.addComponent(TransformComponent.getComponentType(), new TransformComponent(targetPosition, rotation));
        holder.ensureComponent(UUIDComponent.getComponentType());
        ModelAsset modelAsset = ModelAsset.getAssetMap().getAsset("Test");
        if (modelAsset == null)
            modelAsset = ModelAsset.DEBUG;

        Model model = Model.createStaticScaledModel(modelAsset, 2);

        holder.addComponent(PersistentModel.getComponentType(), new PersistentModel(model.toReference()));
        holder.addComponent(ModelComponent.getComponentType(), new ModelComponent(model));
        Box boundingBox = model.getBoundingBox();
        if (boundingBox == null)
            boundingBox = HORIZONTALLY_CENTERED;
        holder.addComponent(BoundingBox.getComponentType(), new BoundingBox(boundingBox));
        holder.putComponent(PhysicsBlockComponent.getComponentType(), new PhysicsBlockComponent());
        holder.addComponent(Velocity.getComponentType(), new Velocity());
        return holder;
    }

    @Override
    protected void interactWithBlock(@Nonnull World world, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull InteractionType type, @Nonnull InteractionContext context, @Nullable ItemStack stack, @Nonnull Vector3i targetBlock, @Nonnull CooldownHandler cooldown) {
        WorldChunk chunk = world.getChunkIfInMemory(ChunkUtil.indexChunkFromBlock(targetBlock.x, targetBlock.z));
        if (chunk != null) {
            commandBuffer.addEntity(getFallingBlockHolder(targetBlock), AddReason.SPAWN);
        }
    }

    @Override
    protected void simulateInteractWithBlock(@Nonnull InteractionType interactionType, @Nonnull InteractionContext interactionContext, @Nullable ItemStack itemStack, @Nonnull World world, @Nonnull Vector3i vector3i) {

    }
}
