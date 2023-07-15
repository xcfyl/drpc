package com.github.xcfyl.drpc.core.server;

import com.github.xcfyl.drpc.core.filter.server.ServerFilterChain;
import com.github.xcfyl.drpc.core.registry.ProviderData;
import com.github.xcfyl.drpc.core.registry.Registry;
import com.github.xcfyl.drpc.core.serializer.Serializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * rpc服务端的上下文，用于存放一些公共参数
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:29
 */
public class ServerContext {
    /**
     * 存放服务名称和服务提供者class的映射
     */
    private final Map<String, Object> serviceProviderCache = new ConcurrentHashMap<>();
    /**
     * 存放本地服务注册信息的缓存
     */
    private final Map<String, ProviderData> registryDataCache = new ConcurrentHashMap<>();
    /**
     * 服务端的缓存
     */
    private ServerConfig serverConfig;
    /**
     * 序列化工厂
     */
    private Serializer serializer;
    /**
     * 服务端的过滤器
     */
    private ServerFilterChain filterChain;
    /**
     * 注册中心
     */
    private Registry registry;
    /**
     * 配置文件的路径
     */
    private String configFileName;

    public Map<String, Object> getServiceProviderCache() {
        return serviceProviderCache;
    }

    public Map<String, ProviderData> getRegistryDataCache() {
        return registryDataCache;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public ServerFilterChain getFilterChain() {
        return filterChain;
    }

    public void setFilterChain(ServerFilterChain filterChain) {
        this.filterChain = filterChain;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }
}
