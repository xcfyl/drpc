package com.github.xcfyl.drpc.core.protocol;


import java.util.HashMap;
import java.util.Map;

/**
 * rpc响应
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:42
 */
public class RpcResponse {
    /**
     * 和响应对应的请求id保持一致
     */
    private String id;
    /**
     * 响应体
     */
    private Object body;
    /**
     * 本次响应附加的属性，额外扩展字段
     */
    private Map<String, Object> attr;

    public RpcResponse() {

    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}