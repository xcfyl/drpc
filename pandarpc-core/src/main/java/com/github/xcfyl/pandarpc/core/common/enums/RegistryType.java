package com.github.xcfyl.pandarpc.core.common.enums;

/**
 * @author 西城风雨楼
 */
public enum RegistryType implements PandaRpcEnum {
    ZK(0, "zookeeper");

    private final int code;
    private final String description;

    RegistryType(int code, String description) {
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

    public static RegistryType fromCode(int code) {
        for (RegistryType registryType : values()) {
            if (registryType.code == code) {
                return registryType;
            }
        }
        throw new RuntimeException("code转RegistryType失败");
    }

    public static RegistryType fromDescription(String description) {
        for (RegistryType registryType : values()) {
            if (registryType.description.equals(description)) {
                return registryType;
            }
        }
        throw new RuntimeException("description转RegistryType失败");
    }
}
