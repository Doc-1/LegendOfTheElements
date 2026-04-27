package dev.docvin.legendofelements.commands;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.commands.block.SimpleBlockCommand;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import dev.docvin.legendofelements.chunk.blocks.components.LockableBlockComponent;

import javax.annotation.Nonnull;

public class SpawnItemLock extends SimpleBlockCommand {
    private final RequiredArg<String> keyId = this.withRequiredArg("keyId", "The Id that this key will unlock", ArgTypes.STRING);
    private final RequiredArg<Boolean> consume = this.withRequiredArg("consume", "If the key should ignore the locks consume rule", ArgTypes.BOOLEAN);
    private final RequiredArg<Boolean> locked = this.withRequiredArg("locked", "If the door should be locked or not", ArgTypes.BOOLEAN);

    public SpawnItemLock() {
        super("createLock", "");
    }


    @Override
    protected void executeWithBlock(@Nonnull CommandContext context, @Nonnull WorldChunk chunk, int x, int y, int z) {
        CommandSender sender = context.sender();
        int blockId = chunk.getBlock(x, y, z);
        Store<ChunkStore> chunkStore = chunk.getWorld().getChunkStore().getStore();
        BlockComponentChunk blockComponentChunk = chunkStore.getComponent(chunk.getReference(), BlockComponentChunk.getComponentType());

        if (blockComponentChunk != null) {
            Ref<ChunkStore> blockRef = blockComponentChunk.getEntityReference(blockId);
            LockableBlockComponent lockableBlockComponent = new LockableBlockComponent();
            lockableBlockComponent.setLocked(locked.get(context));
            lockableBlockComponent.setConsumeKey(consume.get(context));
            lockableBlockComponent.setKeyId(keyId.get(context));
            if (blockRef == null || !blockRef.isValid()) {
                Holder<ChunkStore> holder = ChunkStore.REGISTRY.newHolder();
                holder.putComponent(BlockModule.BlockStateInfo.getComponentType(), new BlockModule.BlockStateInfo(blockId, chunk.getReference()));
                holder.putComponent(LockableBlockComponent.getComponentType(), lockableBlockComponent);
                chunkStore.addEntity(holder, AddReason.SPAWN);
            } else
                chunkStore.putComponent(blockRef, LockableBlockComponent.getComponentType(), lockableBlockComponent);
        }
        sender.sendMessage(Message.raw("locked door"));
    }
}
