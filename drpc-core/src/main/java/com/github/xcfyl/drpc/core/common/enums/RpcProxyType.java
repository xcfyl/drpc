package com.github.xcfyl.drpc.core.common.enums;

/**
 * @author 西城风雨楼
 */
public enum RpcProxyType implements RpcEnum {
    JDK(0, "jdk");

    private final int code;

    private final String description;

    RpcProxyType(int code, String description) {
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

    public static RpcProxyType fromCode(int code) {
        for (RpcProxyType proxyType : values()) {
            if (proxyType.code == code) {
                return proxyType;
            }
        }
        throw new RuntimeException("code转ProxyType失败");
    }

    public static RpcProxyType fromDescription(String description) {
        for (RpcProxyType proxyType : values()) {
            if (proxyType.getDescription().equals(description)) {
                return proxyType;
            }
        }
        throw new RuntimeException("description转ProxyType失败");
    }
}
