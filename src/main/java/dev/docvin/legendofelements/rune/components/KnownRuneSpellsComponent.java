package dev.docvin.legendofelements.rune.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.docvin.legendofelements.registry.data.Component;
import dev.docvin.legendofelements.rune.assets.RunicSpell;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A component for player entities to keep track of the rune spells they have learnt.
 */
public class KnownRuneSpellsComponent implements Component<KnownRuneSpellsComponent, EntityStore> {


    public static final BuilderCodec<KnownRuneSpellsComponent> CODEC = BuilderCodec.builder(KnownRuneSpellsComponent.class, KnownRuneSpellsComponent::new)
            .append(new KeyedCodec<>("KnownRuneSpells", Codec.STRING_ARRAY),
                    (c, v) -> c.knownSpells = v, c -> c.knownSpells)
            .add()
            .append(new KeyedCodec<>("KnownRunicSpells", RunicSpell.ARRAY_CODEC), (c, v) -> c.spells = v, c -> c.spells)
            .add()
            .build();
    private static ComponentType<EntityStore, KnownRuneSpellsComponent> componentType;
    private String[] knownSpells;
    private RunicSpell[] spells;

    public static ComponentType<EntityStore, KnownRuneSpellsComponent> getComponentType() {
        return componentType;
    }

    @Override
    public void setComponentType(ComponentType<EntityStore, KnownRuneSpellsComponent> staticComponentType) {
        componentType = staticComponentType;
    }

    @Override
    public String getComponentId() {

        return "";
    }

    public void learnRunicSpell(RunicSpell spell) {
        List<RunicSpell> list = spells == null ? new ArrayList<>() : new ArrayList<>(List.of(spells));
        if (!knowsRuneSpell(spell)) {
            list.add(spell);
            spells = list.toArray(RunicSpell[]::new);
        }
    }

//    public void learnRuneSpell(RunicSpell spell) throws AlreadyKnowsRuneSpellException {
//        List<String> list = knownSpells == null ? new ArrayList<>() : new ArrayList<>(List.of(knownSpells));
//        if (!knowsRuneSpell(spell)) {
//            list.add(spell.getSpellName());
//            knownSpells = list.toArray(String[]::new);
//        } else
//            throw new AlreadyKnowsRuneSpellException(spell);
//    }

    public boolean knowsRuneSpell(RunicSpell spell) {
        if (knownSpells == null)
            return false;
        for (RunicSpell runicSpell : spells)
            return runicSpell.getSpellName().equals(spell.getSpellName());
        return false;
    }

    public RunicSpell[] getKnownSpells() {
        return spells;
    }

    @Nullable
    @Override
    public com.hypixel.hytale.component.Component<EntityStore> clone() {
        KnownRuneSpellsComponent knownRuneSpellsComponent = new KnownRuneSpellsComponent();
        knownRuneSpellsComponent.spells = this.spells;
        return knownRuneSpellsComponent;
    }


}
