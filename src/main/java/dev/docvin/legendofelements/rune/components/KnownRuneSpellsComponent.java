package dev.docvin.legendofelements.rune.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.rune.assets.RuneSpellsAsset;
import dev.docvin.legendofelements.rune.exception.AlreadyKnowsRuneSpellException;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class KnownRuneSpellsComponent implements Component<EntityStore> {

    public static final BuilderCodec<KnownRuneSpellsComponent> CODEC =
            BuilderCodec.builder(KnownRuneSpellsComponent.class, KnownRuneSpellsComponent::new)
                    .append(new KeyedCodec<>("KnownRuneSpells", Codec.STRING_ARRAY),
                            (c, v) -> c.knownSpells = v, c -> c.knownSpells)
                    .add()
                    .build();
    private static ComponentType<EntityStore, KnownRuneSpellsComponent> type;
    private String[] knownSpells;

    public static ComponentType<EntityStore, KnownRuneSpellsComponent> getComponentType() {
        return type;
    }

    public static void setComponentType(ComponentType<EntityStore, KnownRuneSpellsComponent> type) {
        KnownRuneSpellsComponent.type = type;
    }

    public void learnRuneSpell(RuneSpellsAsset spell) throws AlreadyKnowsRuneSpellException {
        List<String> list = knownSpells == null ? new ArrayList<>() : new ArrayList<>(List.of(knownSpells));
        if (!knowsRuneSpell(spell)) {
            list.add(spell.getName());
            knownSpells = list.toArray(String[]::new);
        } else
            throw new AlreadyKnowsRuneSpellException(spell);
    }


    public boolean knowsRuneSpell(RuneSpellsAsset spell) {
        if (knownSpells == null)
            return false;
        for (String name : knownSpells)
            return name.equals(spell.getName());
        return false;
    }

    public String[] getKnownSpells() {
        return knownSpells;
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        return null;
    }

}
