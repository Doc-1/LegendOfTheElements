package dev.docvin.legendofelements.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.transaction.ItemStackTransaction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.items.ItemKeyComponent;

import javax.annotation.Nonnull;

public class SpawnItemKey extends AbstractPlayerCommand {
    private final RequiredArg<Item> itemArg = this.withRequiredArg("item", "server.commands.give.item.desc", ArgTypes.ITEM_ASSET);
    private final RequiredArg<String> keyId = this.withRequiredArg("keyId", "The Id that this key will unlock", ArgTypes.STRING);
    private final RequiredArg<Boolean> overrideConsume = this.withRequiredArg("overrideConsume", "If the key should ignore the locks consume rule", ArgTypes.BOOLEAN);

    public SpawnItemKey() {
        super("createKey", "description");
    }

    @Override
    protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player playerComponent = store.getComponent(ref, Player.getComponentType());

        assert playerComponent != null;

        Item item = this.itemArg.get(context);
        ItemKeyComponent itemKeyComponent = new ItemKeyComponent();
        itemKeyComponent.setOverrideConsume(overrideConsume.get(context));
        itemKeyComponent.setKeyIds(new String[]{keyId.get(context)});
        ItemStack stack = new ItemStack(item.getId(), 1);
        stack = stack.withMetadata(itemKeyComponent.getComponentId(), itemKeyComponent.getCodec(), itemKeyComponent);
        ItemStackTransaction transaction = playerComponent.giveItem(stack, ref, store);
        ItemStack remainder = transaction.getRemainder();
        Message itemNameMessage = Message.translation(item.getTranslationKey());
        if (remainder != null && !remainder.isEmpty()) {
            context.sendMessage(Message.translation("server.commands.give.insufficientInvSpace").param("quantity", 1).param("item", itemNameMessage));
        } else {
            context.sendMessage(Message.translation("server.commands.give.received").param("quantity", 1).param("item", itemNameMessage));
        }
    }
}
