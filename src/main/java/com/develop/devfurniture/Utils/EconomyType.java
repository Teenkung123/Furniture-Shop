package com.develop.devfurniture.Utils;

public enum EconomyType {
    PLAYERPOINTS, VAULT, BOTH, UNKNOWN;

    public static EconomyType fromString(String str) {
        try {
            return EconomyType.valueOf(EconomyType.class, str.toUpperCase());
        } catch (Exception ex) {
            return UNKNOWN;
        }
    }

    public String toString() {
        return this.name().toLowerCase();
    }
}
