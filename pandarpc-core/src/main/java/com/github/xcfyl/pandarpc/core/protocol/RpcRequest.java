package com.github.xcfyl.pandarpc.core.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc请求
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:41
 */
public class RpcRequest {
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
    private Map<String, Object> attr;

    public RpcRequest() {

    }

    public RpcRequest(String id, String service, String method, Object[] args) {
        this.id = id;
        this.serviceName = service;
        this.methodName = method;
        this.args = args;
        attr = new HashMap<>();
    }

    public void putAttr(String key, Object value) {
        attr.put(key, value);
    }

    public Object getAttr(String key) {
        return attr.get(key);
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getId() {
        return id;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getServiceName() {
        return serviceName;
    }
}

