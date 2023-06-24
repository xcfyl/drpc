package com.github.xcfyl.pandarpc.core.server;

import com.github.xcfyl.pandarpc.core.common.config.RpcServerConfig;
import com.github.xcfyl.pandarpc.core.serialize.RpcSerializeFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc服务端的上下文，用于存放一些公共参数
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:29
 */
public class RpcServerContext {
    /**
     * 存放服务名称和服务提供者class的映射
     */
    private static final Map<String, Object> SERVICE_PROVIDER_CACHE = new HashMap<>();

    /**
     * 存放本地服务注册信息的缓存
     */
    private static final Map<String, Object> REGISTRY_DATA_CACHE = new HashMap<>();

    private static RpcServerConfig rpcServerConfig;

    public static RpcSerializeFactory<?> serializeFactory;

    public static void setSerializeFactory(RpcSerializeFactory<?> serializeFactory) {
        RpcServerContext.serializeFactory = serializeFactory;
    }

    public static RpcSerializeFactory<?> getSerializeFactory() {
        return serializeFactory;
    }

    public static void setRpcServerConfig(RpcServerConfig rpcServerConfig) {
        RpcServerContext.rpcServerConfig = rpcServerConfig;
    }

    public static RpcServerConfig getRpcServerConfig() {
        return rpcServerConfig;
    }

    public static Map<String, Object> getRegistryDataCache() {
        return REGISTRY_DATA_CACHE;
    }

    public static Map<String, Object> getServiceProviderCache() {
        return SERVICE_PROVIDER_CACHE;
    }
}
