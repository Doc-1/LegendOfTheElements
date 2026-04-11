package dev.docvin.legendofelements.registry;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import dev.docvin.legendofelements.SendMessageInteraction;
import dev.docvin.legendofelements.blocks.puzzle.sliding.SlideBlockInteraction;
import dev.docvin.legendofelements.rune.spells.active.CastUpdraftInteractions;

public class AllCodecs implements AllRegistries {

    public static void registerInteractions() {
        AllRegistries.getCodecRegistry(Interaction.CODEC).register("my_custom_interaction_id", SendMessageInteraction.class, SendMessageInteraction.CODEC);
        AllRegistries.getCodecRegistry(Interaction.CODEC).register("Cast_Updraft_0", CastUpdraftInteractions.CastUpdraftFirst.class, CastUpdraftInteractions.CastUpdraftFirst.CODEC);
        //AllRegistries.getCodecRegistry(Interaction.CODEC).register("Cast_Updraft_1", CastUpdraftInteractions.CastUpdraftSecond.class, CastUpdraftInteractions.CastUpdraftSecond.CODEC);
        AllRegistries.getCodecRegistry(Interaction.CODEC).register("Slide_Block_Interaction", SlideBlockInteraction.class, SlideBlockInteraction.CODEC);
    }
}
