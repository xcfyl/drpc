package com.github.xcfyl.drpc.core.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc请求
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:41
 */
public class DrpcRequest {
    /**
     * 当前请求的唯一标识
     */
    private String id;
    /**
     * 服务名
     */
    private String serviceName;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 方法参数
     */
    private Object[] args;
    /**
     * 本次请求的附加字段
     */
    private final Map<String, Object> attrs;

    public DrpcRequest() {
        attrs = new HashMap<>();
    }

    public DrpcRequest(String id, String service, String method, Object[] args) {
        this();
        this.id = id;
        this.serviceName = service;
        this.methodName = method;
        this.args = args;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setAttr(String key, Object value) {
        attrs.put(key, value);
    }

    public Object getAttr(String key) {
        return attrs.get(key);
    }
}

