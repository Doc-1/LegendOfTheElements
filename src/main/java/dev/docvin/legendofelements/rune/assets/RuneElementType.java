package dev.docvin.legendofelements.rune.assets;

public enum RuneElementType {
    WIND("Wind"),
    EARTH("Earth"),
    LIGHTNING("Lightning"),
    WATER("Water"),
    FIRE("Fire"),
    VOID("Void"),
    LIGHT("Light");

    private final String type;

    RuneElementType(String type) {
        this.type = type;
    }

    public static RuneElementType getElementType(String typeName) {
        return switch (typeName) {
            case "Wind" -> WIND;
            case "Earth" -> EARTH;
            case "Lightning" -> LIGHTNING;
            case "Water" -> WATER;
            case "Fire" -> FIRE;
            case "Void" -> VOID;
            case "Light" -> LIGHT;
            default -> null;
        };
    }

    public String getTypeName() {
        return type;
    }

}
