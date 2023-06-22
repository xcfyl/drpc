package com.github.xcfyl.drpc.core.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc请求
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:41
 */
@Getter
public class RpcRequest {
    /**
     * 当前请求的唯一标识
     */
    private final String id;
    /**
     * 服务名
     */
    private final String service;
    /**
     * 方法名
     */
    private final String method;
    /**
     * 方法参数
     */
    private final Object[] args;
    /**
     * 本次请求的附加字段
     */
    private final Map<String, Object> attr;

    public RpcRequest(String id, String service, String method, Object[] args) {
        this.id = id;
        this.service = service;
        this.method = method;
        this.args = args;
        attr = new HashMap<>();
    }

    public void putAttr(String key, Object value) {
        attr.put(key, value);
    }

    public Object getAttr(String key) {
        return attr.get(key);
    }
}

