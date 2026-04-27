package dev.docvin.legendofelements.items.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.chunk.blocks.components.LockableBlockComponent;
import dev.docvin.legendofelements.registry.data.IRegistryDataInteractions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UseLockInteraction extends SimpleBlockInteraction implements IRegistryDataInteractions<UseLockInteraction> {

    @Override
    protected void interactWithBlock(@Nonnull World world, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull InteractionType type, @Nonnull InteractionContext context, @Nullable ItemStack itemStack, @Nonnull Vector3i targetBlock, @Nonnull CooldownHandler cooldown) {

        WorldChunk chunk = world.getChunkIfInMemory(ChunkUtil.indexChunkFromBlock(targetBlock.x, targetBlock.z));
        if (chunk == null)
            return;
        Store<ChunkStore> chunkStore = world.getChunkStore().getStore();
        BlockComponentChunk blockComponentChunk = chunkStore.getComponent(chunk.getReference(), BlockComponentChunk.getComponentType());

        if (blockComponentChunk != null) {
            Ref<ChunkStore> blockRef = blockComponentChunk.getEntityReference(chunk.getBlock(targetBlock));
            if (blockRef == null || !blockRef.isValid()) {
                Holder<ChunkStore> holder = ChunkStore.REGISTRY.newHolder();
                holder.putComponent(BlockModule.BlockStateInfo.getComponentType(), new BlockModule.BlockStateInfo(chunk.getBlock(targetBlock), chunk.getReference()));

                LockableBlockComponent lockableBlockComponent = new LockableBlockComponent();
                lockableBlockComponent.setLocked(true);
                lockableBlockComponent.setConsumeKey(true);
                lockableBlockComponent.setKeyId("Key_1");
                holder.putComponent(LockableBlockComponent.getComponentType(), lockableBlockComponent);
                chunkStore.addEntity(holder, AddReason.SPAWN);
            }
        }
    }


    @Override
    protected void simulateInteractWithBlock(@Nonnull InteractionType interactionType, @Nonnull InteractionContext interactionContext, @Nullable ItemStack itemStack, @Nonnull World world, @Nonnull Vector3i vector3i) {

    }

    @Override
    public String getRegId() {
        return "Add_Lock_Interaction";
    }

    @Override
    public BuilderCodec<UseLockInteraction> getCodec() {
        return BuilderCodec.builder(UseLockInteraction.class, UseLockInteraction::new, SimpleBlockInteraction.CODEC).build();
    }
}
