package com.github.xcfyl.pandarpc.core.enums;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 16:05
 */
public enum RpcServerConfigName implements PandaRpcEnum {
    SERVER_MAX_REQUEST_LENGTH(0, "server.request.limit"),
    SERVER_REGISTRY_TYPE(1, "server.registry"),
    SERVER_PORT(2, "server.port"),
    SERVER_IP(3, "server.ip");

    private final int code;
    private final String description;

    RpcServerConfigName(int code, String description) {
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

    public static RpcServerConfigName fromCode(int code) {
        for (RpcServerConfigName configName : values()) {
            if (configName.code == code) {
                return configName;
            }
        }
        throw new RuntimeException("code转RpcServerConfigName失败");
    }

    public static RpcServerConfigName fromDescription(String description) {
        for (RpcServerConfigName configName : values()) {
            if (configName.description.equals(description)) {
                return configName;
            }
        }
        throw new RuntimeException("description转RpcServerConfigName失败");
    }
}
