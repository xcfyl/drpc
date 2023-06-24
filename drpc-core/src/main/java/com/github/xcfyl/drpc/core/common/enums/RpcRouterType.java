package com.github.xcfyl.drpc.core.common.enums;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 22:55
 */
public enum RpcRouterType implements PandaRpcEnum {
    RANDOM(0, "random"),
    ROUND_ROBIN(1, "roundrobin");

    private final int code;
    private final String description;

    RpcRouterType(int code, String description) {
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

    public static RpcRouterType fromCode(int code) {
        for (RpcRouterType routerType : values()) {
            if (routerType.code == code) {
                return routerType;
            }
        }
        throw new RuntimeException("code转RpcRouterType失败");
    }

    public static RpcRouterType fromDescription(String description) {
        for (RpcRouterType routerType : values()) {
            if (routerType.getDescription().equals(description)) {
                return routerType;
            }
        }
        throw new RuntimeException("description转RpcRouterType失败");
    }
}
