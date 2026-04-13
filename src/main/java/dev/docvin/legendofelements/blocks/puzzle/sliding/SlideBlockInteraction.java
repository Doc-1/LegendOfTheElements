package dev.docvin.legendofelements.blocks.puzzle.sliding;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.BlockFace;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class SlideBlockInteraction extends SimpleBlockInteraction {
    public static final BuilderCodec<SlideBlockInteraction> CODEC = BuilderCodec.builder(
            SlideBlockInteraction.class, SlideBlockInteraction::new, SimpleBlockInteraction.CODEC
    ).build();


    @Override
    protected void interactWithBlock(@Nonnull World world, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull InteractionType type, @Nonnull InteractionContext context, @Nullable ItemStack itemInHand, @Nonnull Vector3i targetBlock, @Nonnull CooldownHandler cooldownHandler) {
        WorldChunk chunk = world.getChunkIfInMemory(ChunkUtil.indexChunkFromBlock(targetBlock.x, targetBlock.z));
        if (chunk == null)
            return;

        if (context.getClientState() != null) {
            BlockType blockType = chunk.getBlockType(targetBlock);
            assert blockType != null;

            String blockState = blockType.getStateForBlock(blockType);
            String defaultBlockState = blockType.getDefaultStateKey();

            if (blockState == null || blockState.equals(defaultBlockState)) {
                BlockFace blockFace = context.getClientState().blockFace;
                String interactionStateToSend = switch (blockFace) {
                    case North -> "Slide_South";
                    case East -> "Slide_West";
                    case South -> "Slide_North";
                    case West -> "Slide_East";
                    default -> "";
                };
                world.setBlockInteractionState(targetBlock, blockType, interactionStateToSend);
                chunk.setTicking(targetBlock.x, targetBlock.y, targetBlock.z, true);
            }
        }
    }

    @Override
    protected void simulateInteractWithBlock(@Nonnull InteractionType var1, @Nonnull InteractionContext var2, @Nullable ItemStack var3, @Nonnull World var4, @Nonnull Vector3i var5) {

    }
}
