package com.github.xcfyl.drpc.core.protocol;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc响应
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:42
 */
@Data
@NoArgsConstructor
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
     * 本次响应附加的属性，额外扩展字段
     */
    private Map<String, Object> attr;

    public DrpcResponse(String id, Object body) {
        this.id = id;
        this.body = body;
        attr = new HashMap<>();
    }
}