package com.github.xcfyl.drpc.core.registry.zookeeper;

import com.github.xcfyl.drpc.core.registry.RegistryData;
import com.github.xcfyl.drpc.core.registry.RpcRegistry;

import java.util.List;

import static com.github.xcfyl.drpc.core.server.RpcServerLocalCache.REGISTRY_DATA_CACHE;

/**
 * 基于zookeeper实现的注册中心
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 20:54
 */
public class ZookeeperRegistry implements RpcRegistry {
    private static final String ROOT = "/drpc";

    private final ZookeeperClient zkClient;

    public ZookeeperRegistry(ZookeeperClient zkClient) {
        this.zkClient = zkClient;
    }

    @Override
    public void register(RegistryData registryData) {
        if (!zkClient.existNode(ROOT)) {
            // 如果不存在根节点，那么尝试创建
            zkClient.createPersistentData(ROOT, "");
        }
        // 走到这里根节点一定是存在的了
        String providerMetaData = RegistryDataZkHelper.getProviderMetaData(registryData);
        String providerNodePath = RegistryDataZkHelper.getProviderNodePath(ROOT, registryData);
        if (zkClient.existNode(providerNodePath)) {
            // 如果当前服务提供者路径已经存在了，那么就将其删除掉
            zkClient.deleteNode(providerNodePath);
        }
        // 创建服务提供者的路径
        zkClient.createTemporaryData(providerNodePath, providerMetaData);
        // 到这里就注册成功了，将注册成功的信息，添加到服务端的本地缓存中
        REGISTRY_DATA_CACHE.put(registryData.getApplicationName(), registryData);
    }

    @Override
    public void unregister(RegistryData registryData) {
        String providerNodePath = RegistryDataZkHelper.getProviderNodePath(ROOT, registryData);
        zkClient.deleteNode(providerNodePath);
        REGISTRY_DATA_CACHE.remove(registryData.getServiceName());
    }

    @Override
    public void subscribe(String serviceName) {

    }

    @Override
    public void unsubscribe(String serviceName) {

    }

    @Override
    public List<RegistryData> queryProviders(String serviceName) {
        return null;
    }

    @Override
    public List<RegistryData> queryConsumers(String serviceName) {
        return null;
    }
}
