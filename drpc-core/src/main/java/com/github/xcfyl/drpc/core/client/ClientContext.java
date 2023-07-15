package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.filter.client.ClientFilterChain;
import com.github.xcfyl.drpc.core.protocol.RpcResponse;
import com.github.xcfyl.drpc.core.proxy.RpcProxy;
import com.github.xcfyl.drpc.core.registry.ConsumerData;
import com.github.xcfyl.drpc.core.registry.Registry;
import com.github.xcfyl.drpc.core.router.Router;
import com.github.xcfyl.drpc.core.serializer.Serializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * rpc客户端上下文，用于存放一些公共参数
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 12:24
 */
public class ClientContext {
    /**
     * 缓存rpc调用结果，key是请求id，value是本次请求的响应数据
     */
    private final Map<String, RpcResponse> responseCache = new ConcurrentHashMap<>();
    /**
     * 缓存当前客户端本地缓存的注册数据
     */
    private final Map<String, ConsumerData> registerDataCache = new HashMap<>();
    /**
     * 连接管理器
     */
    private ConnectionManager connectionManager;
    /**
     * 当前rpc客户端使用的路由器
     */
    private Router router;
    /**
     * 当前Rpc客户端的配置数据
     */
    private ClientConfig clientConfig;
    /**
     * 序列化工厂
     */
    private Serializer serializer;
    /**
     * 客户端的过滤器
     */
    private ClientFilterChain filterChain;
    /**
     * 客户端的注册中心
     */
    private Registry registry;
    /**
     * 客户端所使用的代理对象
     */
    private RpcProxy rpcProxy;
    /**
     * 客户端的配置文件名称
     */
    private String configFileName;

    public ClientContext() {
    }

    public Map<String, RpcResponse> getResponseCache() {
        return responseCache;
    }

    public Map<String, ConsumerData> getRegisterDataCache() {
        return registerDataCache;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public ClientFilterChain getFilterChain() {
        return filterChain;
    }

    public void setFilterChain(ClientFilterChain filterChain) {
        this.filterChain = filterChain;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
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
