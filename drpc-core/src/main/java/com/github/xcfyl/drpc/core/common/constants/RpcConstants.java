package com.github.xcfyl.drpc.core.common.constants;

/**
 * 存放rpc相关的常量定义
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:06
 */
public enum RpcConstants {
    MAGIC_NUMBER(1998, "rpc请求协议魔数");

    private final int code;
    private final String description;

    RpcConstants(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
