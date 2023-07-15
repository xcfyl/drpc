package com.github.xcfyl.drpc.core.common.enums;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/22 18:12
 */
public enum ClientConfigName implements RpcEnum {
    CLIENT_MAX_REQUEST_LENGTH(0, "client.request.limit"),
    CLIENT_REGISTRY_TYPE(1, "client.registry.type"),
    CLIENT_REGISTRY_ADDR(2, "client.registry.addr"),
    CLIENT_APPLICATION_NAME(3, "client.application.name"),
    CLIENT_SERIALIZE_TYPE(4, "client.serializer"),
    CLIENT_REQUEST_TIMEOUT(0, "client.request.timeout"),
    CLIENT_PROXY_TYPE(1, "client.proxy"),
    CLIENT_ROUTER_TYPE(2, "client.router");

    private final int code;
    private final String description;

    ClientConfigName(int code, String description) {
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

    public static ClientConfigName fromCode(int code) {
        for (ClientConfigName configName : values()) {
            if (configName.code == code) {
                return configName;
            }
        }
        throw new RuntimeException("code转RpcClientConfigName失败");
    }

    public static ClientConfigName fromDescription(String description) {
        for (ClientConfigName configName : values()) {
            if (configName.description.equals(description)) {
                return configName;
            }
        }
        throw new RuntimeException("description转RpcClientConfigName失败");
    }
}
