package dev.docvin.legendofelements.rune.exception;

import dev.docvin.legendofelements.rune.assets.RunicSpell;

public class AlreadyKnowsRuneSpellException extends RuntimeException {

    public AlreadyKnowsRuneSpellException(RunicSpell runeSpell) {
        super("The rune spell " + runeSpell.getName() + " is already known!");
    }

}
