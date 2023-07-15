package com.github.xcfyl.drpc.core.common.enums;

/**
 * @author 西城风雨楼
 */
public enum DrpcProxyType implements DrpcEnum {
    JDK(0, "jdk");

    private final int code;

    private final String description;

    DrpcProxyType(int code, String description) {
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

    public static DrpcProxyType fromCode(int code) {
        for (DrpcProxyType proxyType : values()) {
            if (proxyType.code == code) {
                return proxyType;
            }
        }
        throw new RuntimeException("code转ProxyType失败");
    }

    public static DrpcProxyType fromDescription(String description) {
        for (DrpcProxyType proxyType : values()) {
            if (proxyType.getDescription().equals(description)) {
                return proxyType;
            }
        }
        throw new RuntimeException("description转ProxyType失败");
    }
}
