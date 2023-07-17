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
    CLIENT_ROUTER_TYPE(7, "client.router"),
    /**
     * 客户端订阅失败的时候，重试的次数
     */
    CLIENT_SUBSCRIBE_RETRY_TIMES(8, "client.subscribe.retry.times"),
    /**
     * 订阅失败后的重试间隔
     */
    CLIENT_SUBSCRIBE_RETRY_INTERVAL(9, "client.subscribe.retry.interval"),
    /**
     * 客户端请求失败后重试的次数
     */
    CLIENT_REQUEST_RETRY_TIMES(10, "client.request.retry.times"),
    /**
     * 客户端请求失败后重试的间隔时间
     */
    CLIENT_REQUEST_RETRY_INTERVAL(11, "client.request.retry.interval"),
    /**
     * 客户端连接和远程断连之后，下一次重连的时间间隔
     */
    CLIENT_RECONNECT_INTERVAL(12, "client.reconnect.interval"),
    /**
     * 客户端连接和远程断连之后，尝试重连的次数
     */
    CLIENT_RECONNECT_TIMES(13, "client.reconnect.times");

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
