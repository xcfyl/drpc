package com.github.xcfyl.drpc.core.common.enums;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 22:55
 */
public enum DrpcRouterType implements DrpcEnum {
    RANDOM(0, "random"),
    ROUND_ROBIN(1, "roundrobin");

    private final int code;
    private final String description;

    DrpcRouterType(int code, String description) {
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

    public static DrpcRouterType fromCode(int code) {
        for (DrpcRouterType routerType : values()) {
            if (routerType.code == code) {
                return routerType;
            }
        }
        throw new RuntimeException("code转RpcRouterType失败");
    }

    public static DrpcRouterType fromDescription(String description) {
        for (DrpcRouterType routerType : values()) {
            if (routerType.getDescription().equals(description)) {
                return routerType;
            }
        }
        throw new RuntimeException("description转RpcRouterType失败");
    }
}
