package com.github.xcfyl.drpc.core.common.enums;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 16:05
 */
public enum DrpcServerConfigName implements DrpcEnum {
    SERVER_MAX_REQUEST_LENGTH(0, "server.request.limit"),
    SERVER_REGISTRY_TYPE(1, "server.registry.type"),
    SERVER_REGISTRY_ADDR(2, "server.registry.addr"),
    SERVER_APPLICATION_NAME(3, "server.application.name"),
    SERVER_SERIALIZE_TYPE(4, "server.serializer"),
    SERVER_PORT(0, "server.port");

    private final int code;
    private final String description;

    DrpcServerConfigName(int code, String description) {
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

    public static DrpcServerConfigName fromCode(int code) {
        for (DrpcServerConfigName configName : values()) {
            if (configName.code == code) {
                return configName;
            }
        }
        throw new RuntimeException("code转RpcServerConfigName失败");
    }

    public static DrpcServerConfigName fromDescription(String description) {
        for (DrpcServerConfigName configName : values()) {
            if (configName.description.equals(description)) {
                return configName;
            }
        }
        throw new RuntimeException("description转RpcServerConfigName失败");
    }
}
