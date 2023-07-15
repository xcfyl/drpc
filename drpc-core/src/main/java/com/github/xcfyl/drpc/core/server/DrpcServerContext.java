package com.github.xcfyl.drpc.core.server;

import com.github.xcfyl.drpc.core.filter.server.DrpcServerFilterChain;
import com.github.xcfyl.drpc.core.registry.DrpcProviderData;
import com.github.xcfyl.drpc.core.registry.DrpcRegistry;
import com.github.xcfyl.drpc.core.serializer.DrpcSerializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * rpc服务端的上下文，用于存放一些公共参数
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:29
 */
public class DrpcServerContext {
    /**
     * 存放服务名称和服务提供者class的映射
     */
    private final Map<String, Object> serviceProviderCache = new ConcurrentHashMap<>();
    /**
     * 存放本地服务注册信息的缓存
     */
    private final Map<String, DrpcProviderData> registryDataCache = new ConcurrentHashMap<>();
    /**
     * 服务端的缓存
     */
    private DrpcServerConfig serverConfig;
    /**
     * 序列化工厂
     */
    private DrpcSerializer serializer;
    /**
     * 服务端的过滤器
     */
    private DrpcServerFilterChain filterChain;
    /**
     * 注册中心
     */
    private DrpcRegistry registry;
    /**
     * 配置文件的路径
     */
    private String configFileName;

    public Map<String, Object> getServiceProviderCache() {
        return serviceProviderCache;
    }

    public Map<String, DrpcProviderData> getRegistryDataCache() {
        return registryDataCache;
    }

    public DrpcServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(DrpcServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public DrpcSerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(DrpcSerializer serializer) {
        this.serializer = serializer;
    }

    public DrpcServerFilterChain getFilterChain() {
        return filterChain;
    }

    public void setFilterChain(DrpcServerFilterChain filterChain) {
        this.filterChain = filterChain;
    }

    public DrpcRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(DrpcRegistry registry) {
        this.registry = registry;
    }

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }
}
