package dev.docvin.legendofelements.block;

import com.hypixel.hytale.builtin.mounts.minecart.MinecartComponent;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
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
import com.hypixel.hytale.server.core.modules.entity.component.*;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpawnFallingBlockInteraction extends SimpleBlockInteraction {
    public static final BuilderCodec<SpawnFallingBlockInteraction> CODEC = BuilderCodec.builder(
            SpawnFallingBlockInteraction.class, SpawnFallingBlockInteraction::new, SimpleBlockInteraction.CODEC
    ).build();

    @Override
    protected void interactWithBlock(@Nonnull World world, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull InteractionType type, @Nonnull InteractionContext context, @Nullable ItemStack stack, @Nonnull Vector3i targetBlock, @Nonnull CooldownHandler cooldown) {

        Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();
        Vector3d targetPosition = targetBlock.toVector3d();
        targetPosition.add(0.5, 1, 0.5);
        Vector3f rotation = new Vector3f(0F, (float) Math.PI, 0F);


        WorldChunk chunk = world.getChunkIfInMemory(ChunkUtil.indexChunkFromBlock(targetBlock.x, targetBlock.z));
        if (chunk != null) {


            holder.addComponent(TransformComponent.getComponentType(), new TransformComponent(targetPosition, rotation));
            holder.ensureComponent(UUIDComponent.getComponentType());
            ModelAsset modelAsset = ModelAsset.getAssetMap().getAsset("Test");
            if (modelAsset == null)
                modelAsset = ModelAsset.DEBUG;


            Model model = Model.createStaticScaledModel(modelAsset, 2);

            holder.addComponent(PersistentModel.getComponentType(), new PersistentModel(model.toReference()));
            holder.addComponent(ModelComponent.getComponentType(), new ModelComponent(model));
            holder.addComponent(BoundingBox.getComponentType(), new BoundingBox(model.getBoundingBox()));
            holder.ensureComponent(Interactable.getComponentType());
            holder.putComponent(
                    MinecartComponent.getComponentType(), new MinecartComponent(context.getHeldItem() != null ? context.getHeldItem().getItemId() : null)
            );
            commandBuffer.addEntity(holder, AddReason.SPAWN);
        }
    }

    @Override
    protected void simulateInteractWithBlock(@Nonnull InteractionType interactionType, @Nonnull InteractionContext interactionContext, @Nullable ItemStack itemStack, @Nonnull World world, @Nonnull Vector3i vector3i) {

    }
}
