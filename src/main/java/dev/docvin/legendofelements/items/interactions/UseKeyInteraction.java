package dev.docvin.legendofelements.items.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.chunk.blocks.components.LockableBlockComponent;
import dev.docvin.legendofelements.items.ItemKeyComponent;
import dev.docvin.legendofelements.registry.data.IRegistryDataInteractions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * This interaction contains the logic for when an item that has a matching keyId from a {@link ItemKeyComponent} unlocks an action designated by the developer with a {@link LockableBlockComponent}.
 * This is to be used in tandem with {@link LockableBlockComponent} and {@link ItemKeyComponent}
 */
public class UseKeyInteraction extends SimpleBlockInteraction implements IRegistryDataInteractions<UseKeyInteraction> {

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
                holder.ensureComponent(LockableBlockComponent.getComponentType());
                blockRef = chunkStore.addEntity(holder, AddReason.SPAWN);
            }

            if (blockRef != null && blockRef.isValid()) {
                LockableBlockComponent lockableBlockComponent = chunkStore.getComponent(blockRef, LockableBlockComponent.getComponentType());
                if (lockableBlockComponent != null) {

                    assert itemStack != null;
                    ItemKeyComponent keyComponent = itemStack.getFromMetadataOrNull("Key", ItemKeyComponent.CODEC);
                    assert keyComponent != null;

                    String keyId = lockableBlockComponent.getKeyId();
                    boolean consumeKey = lockableBlockComponent.isConsumeKey();
                    boolean idMatch = false;
                    if (keyId != null)
                        for (String id : keyComponent.getKeyIds()) {
                            if (id.startsWith("\"") && id.endsWith("\""))
                                id = id.substring(1, id.length() - 1);
                            if (keyId.equals(id)) {
                                idMatch = true;
                                break;
                            }
                        }
                    if (idMatch) {
                        int currentQuantity = itemStack.getQuantity();
                        if (!keyComponent.isOverrideConsume() || consumeKey && currentQuantity >= 1) {
                            ItemStack newStack = itemStack.withQuantity(currentQuantity - 1);
                            InventoryComponent.Hotbar itemContainer = (InventoryComponent.Hotbar) commandBuffer.getComponent(context.getOwningEntity(), Objects.requireNonNull(InventoryComponent.getComponentTypeById(InventoryComponent.HOTBAR_SECTION_ID)));
                            assert itemContainer != null;
                            ItemContainer hotbar = itemContainer.getInventory();
                            hotbar.setItemStackForSlot(itemContainer.getActiveSlot(), newStack);
                        }
                        lockableBlockComponent.setLocked(!lockableBlockComponent.isLocked());
                    }
                }
            }
        }

    }

    @Override
    protected void simulateInteractWithBlock(@Nonnull InteractionType interactionType, @Nonnull InteractionContext interactionContext, @Nullable ItemStack itemStack, @Nonnull World world, @Nonnull Vector3i vector3i) {

    }

    @Override
    public String getRegId() {
        return "Use_Key_Interaction";
    }

    @Override
    public BuilderCodec<UseKeyInteraction> getCodec() {
        return BuilderCodec.builder(UseKeyInteraction.class, UseKeyInteraction::new, SimpleBlockInteraction.CODEC)
                .build();
    }
}
