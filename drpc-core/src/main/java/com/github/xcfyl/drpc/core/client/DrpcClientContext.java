package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.filter.client.DrpcClientFilterChain;
import com.github.xcfyl.drpc.core.protocol.DrpcResponse;
import com.github.xcfyl.drpc.core.proxy.DrpcProxy;
import com.github.xcfyl.drpc.core.registry.DrpcConsumerData;
import com.github.xcfyl.drpc.core.registry.Registry;
import com.github.xcfyl.drpc.core.router.DrpcRouter;
import com.github.xcfyl.drpc.core.serializer.DrpcSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * rpc客户端上下文，用于存放一些公共参数
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 12:24
 */
public class DrpcClientContext {
    /**
     * 缓存rpc调用结果，key是请求id，value是本次请求的响应数据
     */
    private final Map<String, DrpcResponse> responseCache = new ConcurrentHashMap<>();
    /**
     * 缓存当前客户端本地缓存的注册数据
     */
    private final Map<String, DrpcConsumerData> registerDataCache = new HashMap<>();
    /**
     * 连接管理器
     */
    private DprcConnectionManager connectionManager;
    /**
     * 当前rpc客户端使用的路由器
     */
    private DrpcRouter router;
    /**
     * 当前Rpc客户端的配置数据
     */
    private DrpcClientConfig clientConfig;
    /**
     * 序列化工厂
     */
    private DrpcSerializer serializer;
    /**
     * 客户端的过滤器
     */
    private DrpcClientFilterChain filterChain;
    /**
     * 客户端的注册中心
     */
    private Registry registry;
    /**
     * 客户端所使用的代理对象
     */
    private DrpcProxy rpcProxy;
    /**
     * 客户端的配置文件名称
     */
    private String configFileName;

    public DrpcClientContext() {
    }

    public Map<String, DrpcResponse> getResponseCache() {
        return responseCache;
    }

    public Map<String, DrpcConsumerData> getRegisterDataCache() {
        return registerDataCache;
    }

    public DrpcRouter getRouter() {
        return router;
    }

    public void setRouter(DrpcRouter router) {
        this.router = router;
    }

    public DrpcClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(DrpcClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public DrpcSerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(DrpcSerializer serializer) {
        this.serializer = serializer;
    }

    public DrpcClientFilterChain getFilterChain() {
        return filterChain;
    }

    public void setFilterChain(DrpcClientFilterChain filterChain) {
        this.filterChain = filterChain;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public DprcConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(DprcConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public DrpcProxy getRpcProxy() {
        return rpcProxy;
    }

    public void setRpcProxy(DrpcProxy rpcProxy) {
        this.rpcProxy = rpcProxy;
    }

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }
}
