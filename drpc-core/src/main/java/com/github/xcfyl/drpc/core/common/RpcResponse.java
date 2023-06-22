package com.github.xcfyl.drpc.core.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc响应
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:42
 */
@Getter
public class RpcResponse {
    /**
     * 和响应对应的请求id保持一致
     */
    private final String id;
    /**
     * 响应体
     */
    private final Object body;
    /**
     * 本次响应附加的属性，额外扩展字段
     */
    private final Map<String, Object> attr;

    public RpcResponse(String id, Object body) {
        this.id = id;
        this.body = body;
        attr = new HashMap<>();
    }

    public void putAttr(String key, Object value) {
        attr.put(key, value);
    }

    public Object getAttr(String key) {
        return attr.get(key);
    }
}