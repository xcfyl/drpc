package com.github.xcfyl.pandarpc.core.client;

import com.github.xcfyl.pandarpc.core.common.config.RpcClientConfig;
import com.github.xcfyl.pandarpc.core.protocol.RpcResponse;
import com.github.xcfyl.pandarpc.core.registry.RegistryData;
import com.github.xcfyl.pandarpc.core.router.RpcRouter;
import com.github.xcfyl.pandarpc.core.serialize.RpcSerializeFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * rpc客户端上下文，用于存放一些公共参数
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 12:24
 */
@SuppressWarnings("rawtypes")
public class RpcClientContext {
    /**
     * 缓存rpc调用结果，key是请求id，value是本次请求的响应数据
     */
    private static final Map<String, RpcResponse> RESPONSE_CACHE = new ConcurrentHashMap<>();
    /**
     * 缓存当前客户端本地缓存的注册数据
     */
    private static final Map<String, RegistryData> REGISTRY_DATA_CACHE = new HashMap<>();
    /**
     * 当前rpc客户端使用的路由器
     */
    private static RpcRouter rpcRouter;
    /**
     * 当前Rpc客户端的配置数据
     */
    private static RpcClientConfig rpcClientConfig;

    private static RpcSerializeFactory serializeFactory;

    public static void setSerializeFactory(RpcSerializeFactory serializeFactory) {
        RpcClientContext.serializeFactory = serializeFactory;
    }

    public static RpcSerializeFactory getSerializeFactory() {
        return serializeFactory;
    }

    public static void setRpcClientConfig(RpcClientConfig rpcClientConfig) {
        RpcClientContext.rpcClientConfig = rpcClientConfig;
    }

    public static RpcClientConfig getRpcClientConfig() {
        return rpcClientConfig;
    }

    public static void setRpcRouter(RpcRouter rpcRouter) {
        RpcClientContext.rpcRouter = rpcRouter;
    }

    public static RpcRouter getRpcRouter() {
        return rpcRouter;
    }

    public static Map<String, RpcResponse> getResponseCache() {
        return RESPONSE_CACHE;
    }

    public static Map<String, RegistryData> getRegistryDataCache() {
        return REGISTRY_DATA_CACHE;
    }
}
