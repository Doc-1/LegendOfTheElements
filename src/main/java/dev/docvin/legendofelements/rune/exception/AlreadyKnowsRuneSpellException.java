package dev.docvin.legendofelements.rune.exception;

import dev.docvin.legendofelements.rune.assets.RuneSpellsAsset;

public class AlreadyKnowsRuneSpellException extends RuntimeException {

    public AlreadyKnowsRuneSpellException(RuneSpellsAsset runeSpell) {
        super("The rune spell " + runeSpell.getName() + " is already known!");
    }

}
