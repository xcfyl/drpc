package com.github.xcfyl.drpc.core.protocol;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc请求
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:41
 */
@Data
@NoArgsConstructor
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
    private Map<String, Object> attr;

    public DrpcRequest(String id, String service, String method, Object[] args) {
        this.id = id;
        this.serviceName = service;
        this.methodName = method;
        this.args = args;
        attr = new HashMap<>();
    }
}

