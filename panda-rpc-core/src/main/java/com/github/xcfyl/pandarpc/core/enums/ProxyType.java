package com.github.xcfyl.pandarpc.core.enums;

/**
 * @author 西城风雨楼
 */
public enum ProxyType implements PandaRpcEnum {
    JDK(0, "jdk");

    private final int code;

    private final String description;

    ProxyType(int code, String description) {
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

    public static ProxyType fromCode(int code) {
        for (ProxyType proxyType : values()) {
            if (proxyType.code == code) {
                return proxyType;
            }
        }
        throw new RuntimeException("code转ProxyType失败");
    }

    public static ProxyType fromDescription(String description) {
        for (ProxyType proxyType : values()) {
            if (proxyType.getDescription().equals(description)) {
                return proxyType;
            }
        }
        throw new RuntimeException("description转ProxyType失败");
    }
}
