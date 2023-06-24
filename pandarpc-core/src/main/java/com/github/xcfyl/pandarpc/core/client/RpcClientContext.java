package com.github.xcfyl.pandarpc.core.client;

import com.github.xcfyl.pandarpc.core.common.config.RpcClientConfig;
import com.github.xcfyl.pandarpc.core.filter.client.RpcClientFilterChain;
import com.github.xcfyl.pandarpc.core.protocol.RpcResponse;
import com.github.xcfyl.pandarpc.core.registry.RegistryData;
import com.github.xcfyl.pandarpc.core.router.RpcRouter;
import com.github.xcfyl.pandarpc.core.serializer.RpcSerializer;

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
    private static RpcRouter router;
    /**
     * 当前Rpc客户端的配置数据
     */
    private static RpcClientConfig clientConfig;

    /**
     * 序列化工厂
     */
    private static RpcSerializer serializer;

    /**
     * 客户端的过滤器
     */
    private static RpcClientFilterChain filterChain;

    public static void setFilterChain(RpcClientFilterChain filterChain) {
        RpcClientContext.filterChain = filterChain;
    }

    public static RpcClientFilterChain getFilterChain() {
        return filterChain;
    }

    public static void setSerializer(RpcSerializer serializer) {
        RpcClientContext.serializer = serializer;
    }

    public static RpcSerializer getSerializer() {
        return serializer;
    }

    public static void setClientConfig(RpcClientConfig clientConfig) {
        RpcClientContext.clientConfig = clientConfig;
    }

    public static RpcClientConfig getClientConfig() {
        return clientConfig;
    }

    public static void setRouter(RpcRouter router) {
        RpcClientContext.router = router;
    }

    public static RpcRouter getRouter() {
        return router;
    }

    public static Map<String, RpcResponse> getResponseCache() {
        return RESPONSE_CACHE;
    }

    public static Map<String, RegistryData> getRegistryDataCache() {
        return REGISTRY_DATA_CACHE;
    }
}
