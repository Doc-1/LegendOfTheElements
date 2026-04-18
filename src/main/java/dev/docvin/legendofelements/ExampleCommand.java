package dev.docvin.legendofelements;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.rune.components.KnownRuneSpellsComponent;
import dev.docvin.legendofelements.rune.components.RuneManaComponent;
import dev.docvin.legendofelements.rune.components.RuneManaRegenComponent;

import javax.annotation.Nonnull;

@Deprecated
public class ExampleCommand extends AbstractPlayerCommand {


    public ExampleCommand(String pluginName, String pluginVersion) {
        super("test", "Prints a test message from the " + pluginName + " plugin.");
        this.setPermissionGroup(GameMode.Adventure); // Allows the command to be used by anyone, not just OP

    }

    @Override
    protected void execute(@Nonnull CommandContext var1, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World var5) {

        Player player = store.getComponent(ref, Player.getComponentType());
        RuneManaRegenComponent magic = new RuneManaRegenComponent();
        store.putComponent(ref, RuneManaRegenComponent.getComponentType(), magic);
        store.putComponent(ref, RuneManaComponent.getComponentType(), new RuneManaComponent());
        store.putComponent(ref, KnownRuneSpellsComponent.getComponentType(), new KnownRuneSpellsComponent());
        assert player != null;
        //Objects.requireNonNull(ref.getStore().getComponent(ref, RuneKnownSpellsComponent.getComponentType())).learnRuneSpell(RuneSpellManager.get().getRuneSpell("Updraft"));
        player.sendMessage(Message.raw("You have Elemental Magic Now!").color("Green").bold(true));

    }

}