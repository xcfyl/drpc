package com.github.xcfyl.pandarpc.core.common.enums;

/**
 * @author 西城风雨楼
 */
public enum RpcSerializeType implements PandaRpcEnum {
    JDK(0, "jdk"),
    FASTJSON(1, "fastjson");

    private final int code;
    private final String description;

    RpcSerializeType(int code, String description) {
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

    public static RpcSerializeType fromCode(int code) {
        for (RpcSerializeType serializeType : values()) {
            if (serializeType.code == code) {
                return serializeType;
            }
        }
        throw new RuntimeException("code转ProxyType失败");
    }

    public static RpcSerializeType fromDescription(String description) {
        for (RpcSerializeType serializeType : values()) {
            if (serializeType.getDescription().equals(description)) {
                return serializeType;
            }
        }
        throw new RuntimeException("description转ProxyType失败");
    }
}
