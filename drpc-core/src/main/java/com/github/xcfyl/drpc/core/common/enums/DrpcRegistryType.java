package com.github.xcfyl.drpc.core.common.enums;

/**
 * @author 西城风雨楼
 */
public enum DrpcRegistryType implements DrpcEnum {
    ZK(0, "zookeeper");

    private final int code;
    private final String description;

    DrpcRegistryType(int code, String description) {
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

    public static DrpcRegistryType fromCode(int code) {
        for (DrpcRegistryType registryType : values()) {
            if (registryType.code == code) {
                return registryType;
            }
        }
        throw new RuntimeException("code转RegistryType失败");
    }

    public static DrpcRegistryType fromDescription(String description) {
        for (DrpcRegistryType registryType : values()) {
            if (registryType.description.equals(description)) {
                return registryType;
            }
        }
        throw new RuntimeException("description转RegistryType失败");
    }
}
