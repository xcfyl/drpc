package com.github.xcfyl.drpc.core.common.enums;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/22 18:12
 */
public enum DrpcClientConfigName implements DrpcEnum {
    CLIENT_MAX_REQUEST_LENGTH(0, "client.request.limit"),
    CLIENT_REGISTRY_TYPE(1, "client.registry.type"),
    CLIENT_REGISTRY_ADDR(2, "client.registry.addr"),
    CLIENT_APPLICATION_NAME(3, "client.application.name"),
    CLIENT_SERIALIZE_TYPE(4, "client.serializer"),
    CLIENT_REQUEST_TIMEOUT(5, "client.request.timeout"),
    CLIENT_PROXY_TYPE(6, "client.proxy"),
    CLIENT_ROUTER_TYPE(7, "client.router");

    private final int code;
    private final String description;

    DrpcClientConfigName(int code, String description) {
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

    public static DrpcClientConfigName fromCode(int code) {
        for (DrpcClientConfigName configName : values()) {
            if (configName.code == code) {
                return configName;
            }
        }
        throw new RuntimeException("code转RpcClientConfigName失败");
    }

    public static DrpcClientConfigName fromDescription(String description) {
        for (DrpcClientConfigName configName : values()) {
            if (configName.description.equals(description)) {
                return configName;
            }
        }
        throw new RuntimeException("description转RpcClientConfigName失败");
    }
}
