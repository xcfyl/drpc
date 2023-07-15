package com.github.xcfyl.drpc.core.common.enums;

/**
 * @author 西城风雨楼
 */
public enum DrpcSerializeType implements DrpcEnum {
    JDK(0, "jdk"),
    FASTJSON(1, "fastjson");

    private final int code;
    private final String description;

    DrpcSerializeType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static DrpcSerializeType fromCode(int code) {
        for (DrpcSerializeType serializeType : values()) {
            if (serializeType.code == code) {
                return serializeType;
            }
        }
        throw new RuntimeException("code转ProxyType失败");
    }

    public static DrpcSerializeType fromDescription(String description) {
        for (DrpcSerializeType serializeType : values()) {
            if (serializeType.getDescription().equals(description)) {
                return serializeType;
            }
        }
        throw new RuntimeException("description转ProxyType失败");
    }
}
