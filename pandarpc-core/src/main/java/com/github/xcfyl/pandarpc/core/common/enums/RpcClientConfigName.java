package com.github.xcfyl.pandarpc.core.common.enums;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/22 18:12
 */
public enum RpcClientConfigName implements PandaRpcEnum {
    CLIENT_REQUEST_TIMEOUT(0, "client.request.timeout"),
    CLIENT_PROXY_TYPE(1, "client.proxy"),
    CLIENT_ROUTER_TYPE(2, "client.router");

    private final int code;
    private final String description;

    RpcClientConfigName(int code, String description) {
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

    public static RpcClientConfigName fromCode(int code) {
        for (RpcClientConfigName configName : values()) {
            if (configName.code == code) {
                return configName;
            }
        }
        throw new RuntimeException("code转RpcClientConfigName失败");
    }

    public static RpcClientConfigName fromDescription(String description) {
        for (RpcClientConfigName configName : values()) {
            if (configName.description.equals(description)) {
                return configName;
            }
        }
        throw new RuntimeException("description转RpcClientConfigName失败");
    }
}
