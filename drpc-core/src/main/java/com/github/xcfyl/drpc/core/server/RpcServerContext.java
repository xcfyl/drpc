package com.github.xcfyl.drpc.core.server;

import com.github.xcfyl.drpc.core.filter.server.RpcServerFilterChain;
import com.github.xcfyl.drpc.core.registry.ProviderRegistryData;
import com.github.xcfyl.drpc.core.registry.RpcRegistry;
import com.github.xcfyl.drpc.core.serializer.RpcSerializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private final Map<String, Object> serviceProviderCache = new ConcurrentHashMap<>();
    /**
     * 存放本地服务注册信息的缓存
     */
    private final Map<String, ProviderRegistryData> registryDataCache = new ConcurrentHashMap<>();
    /**
     * 服务端的缓存
     */
    private RpcServerConfig serverConfig;
    /**
     * 序列化工厂
     */
    private RpcSerializer serializer;
    /**
     * 服务端的过滤器
     */
    private RpcServerFilterChain filterChain;
    /**
     * 注册中心
     */
    private RpcRegistry registry;
    /**
     * 配置文件的路径
     */
    private String configFileName;

    public Map<String, Object> getServiceProviderCache() {
        return serviceProviderCache;
    }

    public Map<String, ProviderRegistryData> getRegistryDataCache() {
        return registryDataCache;
    }

    public RpcServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(RpcServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public RpcSerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(RpcSerializer serializer) {
        this.serializer = serializer;
    }

    public RpcServerFilterChain getFilterChain() {
        return filterChain;
    }

    public void setFilterChain(RpcServerFilterChain filterChain) {
        this.filterChain = filterChain;
    }

    public RpcRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(RpcRegistry registry) {
        this.registry = registry;
    }

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }
}
