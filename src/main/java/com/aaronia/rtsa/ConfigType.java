package com.aaronia.rtsa;

/**
 * Types of configuration items in the device config tree.
 */
public enum ConfigType {
    OTHER(0),
    GROUP(1),
    BLOB(2),
    NUMBER(3),
    BOOL(4),
    ENUM(5),
    STRING(6);

    private final int value;

    ConfigType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ConfigType fromValue(int value) {
        for (ConfigType t : values()) {
            if (t.value == value) return t;
        }
        return OTHER;
    }
}
