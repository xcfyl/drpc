package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.filter.client.RpcClientFilterChain;
import com.github.xcfyl.drpc.core.protocol.RpcResponse;
import com.github.xcfyl.drpc.core.proxy.RpcProxy;
import com.github.xcfyl.drpc.core.registry.ConsumerRegistryData;
import com.github.xcfyl.drpc.core.registry.RpcRegistry;
import com.github.xcfyl.drpc.core.router.RpcRouter;
import com.github.xcfyl.drpc.core.serializer.RpcSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * rpc客户端上下文，用于存放一些公共参数
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 12:24
 */
public class RpcClientContext {
    /**
     * 缓存rpc调用结果，key是请求id，value是本次请求的响应数据
     */
    private final Map<String, RpcResponse> responseCache = new ConcurrentHashMap<>();
    /**
     * 缓存当前客户端本地缓存的注册数据
     */
    private final Map<String, ConsumerRegistryData> registerDataCache = new HashMap<>();
    /**
     * 连接管理器
     */
    private ConnectionManager connectionManager;
    /**
     * 当前rpc客户端使用的路由器
     */
    private RpcRouter router;
    /**
     * 当前Rpc客户端的配置数据
     */
    private RpcClientConfig clientConfig;
    /**
     * 序列化工厂
     */
    private RpcSerializer serializer;
    /**
     * 客户端的过滤器
     */
    private RpcClientFilterChain filterChain;
    /**
     * 客户端的注册中心
     */
    private RpcRegistry registry;
    /**
     * 客户端所使用的代理对象
     */
    private RpcProxy rpcProxy;
    /**
     * 客户端的配置文件名称
     */
    private String configFileName;

    public RpcClientContext() {
    }

    public Map<String, RpcResponse> getResponseCache() {
        return responseCache;
    }

    public Map<String, ConsumerRegistryData> getRegisterDataCache() {
        return registerDataCache;
    }

    public RpcRouter getRouter() {
        return router;
    }

    public void setRouter(RpcRouter router) {
        this.router = router;
    }

    public RpcClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(RpcClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public RpcSerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(RpcSerializer serializer) {
        this.serializer = serializer;
    }

    public RpcClientFilterChain getFilterChain() {
        return filterChain;
    }

    public void setFilterChain(RpcClientFilterChain filterChain) {
        this.filterChain = filterChain;
    }

    public RpcRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(RpcRegistry registry) {
        this.registry = registry;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public RpcProxy getRpcProxy() {
        return rpcProxy;
    }

    public void setRpcProxy(RpcProxy rpcProxy) {
        this.rpcProxy = rpcProxy;
    }

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }
}
