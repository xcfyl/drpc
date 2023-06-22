package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.protocol.RpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * rpc客户端本地的缓存
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 12:24
 */
public class RpcClientLocalCache {
    /**
     * 缓存rpc调用结果，key是请求id，value是本次请求的响应数据
     */
    public static final Map<String, RpcResponse> RESPONSE_MAP = new ConcurrentHashMap<>();
}
