package com.github.xcfyl.drpc.core.protocol;


import java.util.HashMap;
import java.util.Map;

/**
 * rpc响应
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:42
 */
public class DrpcResponse {
    /**
     * 和响应对应的请求id保持一致
     */
    private String id;
    /**
     * 响应体
     */
    private Object body;
    /**
     * 请求失败的时候，对应的请求异常
     */
    private Throwable throwable;
    /**
     * 本次响应附加的属性，额外扩展字段
     */
    private Map<String, Object> attrs;

    public DrpcResponse() {

    }

    public DrpcResponse(String id, Object body) {
        this.id = id;
        this.body = body;
        attrs = new HashMap<>();
    }

    public void setAttr(String key, Object value) {
        attrs.put(key, value);
    }

    public Object getAttr(String key) {
        return attrs.get(key);
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

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}