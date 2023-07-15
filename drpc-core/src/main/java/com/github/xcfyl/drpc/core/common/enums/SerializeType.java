package com.github.xcfyl.drpc.core.common.enums;

/**
 * @author 西城风雨楼
 */
public enum SerializeType implements RpcEnum {
    JDK(0, "jdk"),
    FASTJSON(1, "fastjson");

    private final int code;
    private final String description;

    SerializeType(int code, String description) {
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

    public static SerializeType fromCode(int code) {
        for (SerializeType serializeType : values()) {
            if (serializeType.code == code) {
                return serializeType;
            }
        }
        throw new RuntimeException("code转ProxyType失败");
    }

    public static SerializeType fromDescription(String description) {
        for (SerializeType serializeType : values()) {
            if (serializeType.getDescription().equals(description)) {
                return serializeType;
            }
        }
        throw new RuntimeException("description转ProxyType失败");
    }
}
