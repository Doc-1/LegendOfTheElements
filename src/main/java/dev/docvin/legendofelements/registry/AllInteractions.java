package dev.docvin.legendofelements.registry;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import dev.docvin.legendofelements.SendMessageInteraction;
import dev.docvin.legendofelements.chunk.blocks.interactions.LockableDoorInteraction;
import dev.docvin.legendofelements.chunk.blocks.interactions.SlideBlockInteraction;
import dev.docvin.legendofelements.chunk.blocks.interactions.SpawnFallingBlockInteraction;
import dev.docvin.legendofelements.items.interactions.UseKeyInteraction;
import dev.docvin.legendofelements.items.interactions.UseLockInteraction;

import java.lang.reflect.InvocationTargetException;

public class AllInteractions implements AllRegistries {

    public static void registerInteractions() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        AllRegistries.getCodecRegistry(Interaction.CODEC).register("my_custom_interaction_id", SendMessageInteraction.class, SendMessageInteraction.CODEC);
        AllRegistries.getCodecRegistry(Interaction.CODEC).register("Slide_Block_Interaction", SlideBlockInteraction.class, SlideBlockInteraction.CODEC);
        AllRegistries.getCodecRegistry(Interaction.CODEC).register("Spawn_Falling_Block_Interaction", SpawnFallingBlockInteraction.class, SpawnFallingBlockInteraction.CODEC);

        //AllRegistries.getCodecRegistry(Interaction.CODEC).register("Door", LockableDoorInteraction.class, LockableDoorInteraction.CODEC);
        AllRegistries.registerInteraction(LockableDoorInteraction::new);
        AllRegistries.registerInteraction(UseKeyInteraction::new);
        AllRegistries.registerInteraction(UseLockInteraction::new);
    }
}
