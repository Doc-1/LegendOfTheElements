package dev.docvin.legendofelements.registry;

import dev.docvin.legendofelements.rune.assets.RunicSpell;
import dev.docvin.legendofelements.rune.assets.spells.active.UpdraftRunicSpell;

public class AllRuneSpells implements AllRegistries {

    public static void register() {
        //AllRegistries.getRuneSpellRegistry().registerRuneSpell(new UpdraftRuneSpell());
        AllRegistries.getCodecRegistry(RunicSpell.CODEC).register("", UpdraftRunicSpell.class, UpdraftRunicSpell.CODEC);
    }
}
