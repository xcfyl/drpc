package com.github.xcfyl.pandarpc.core.common.enums;

/**
 * rpc通用配置，客户端和服务端公有的配置
 *
 * @author 西城风雨楼
 */
public enum RpcCommonConfigName implements PandaRpcEnum {
    MAX_REQUEST_LENGTH(0, "request.limit"),
    REGISTRY_TYPE(1, "registry.type"),
    REGISTRY_ADDR(2, "registry.addr"),
    APPLICATION_NAME(3, "application.name");

    private final int code;
    private final String description;

    RpcCommonConfigName(int code, String description) {
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

    public static RpcCommonConfigName fromCode(int code) {
        for (RpcCommonConfigName configName : values()) {
            if (configName.code == code) {
                return configName;
            }
        }
        throw new RuntimeException("code转RpcCommonConfigName失败");
    }

    public static RpcCommonConfigName fromDescription(String description) {
        for (RpcCommonConfigName configName : values()) {
            if (configName.description.equals(description)) {
                return configName;
            }
        }
        throw new RuntimeException("description转RpcCommonConfigName失败");
    }
}
