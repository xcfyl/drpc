package com.github.xcfyl.drpc.core.registry.zookeeper;

import com.alibaba.fastjson.JSON;
import com.github.xcfyl.drpc.core.registry.RegistryData;
import com.github.xcfyl.drpc.core.registry.RpcRegistry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.ArrayList;
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
    public void subscribe(RegistryData registryData) {
        if (!zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String consumerMetaData = RegistryDataZkHelper.getConsumerMetaData(registryData);
        String consumerNodePath = RegistryDataZkHelper.getConsumerNodePath(ROOT, registryData);
        if (zkClient.existNode(consumerNodePath)) {
            zkClient.deleteNode(consumerNodePath);
        }
        zkClient.createTemporaryData(consumerNodePath, consumerMetaData);
        String servicePath = RegistryDataZkHelper.getServicePath(ROOT, registryData);
        zkClient.watchChildNodeData(servicePath, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                // 这里监听孩子节点的变化，如果服务列表增加或者减少，那么需要更新本地client
                // 的本地服务缓存列表
            }
        });
    }

    @Override
    public void unsubscribe(RegistryData registryData) {
        String consumerNodePath = RegistryDataZkHelper.getConsumerNodePath(ROOT, registryData);
        zkClient.deleteNode(consumerNodePath);
    }

    @Override
    public List<RegistryData> queryProviders(String serviceName) {
        String servicePath = RegistryDataZkHelper.getServicePath(ROOT, serviceName);
        return queryRegistryDataByPath(servicePath);
    }

    @Override
    public List<RegistryData> queryConsumers(String serviceName) {
        String consumerPath = RegistryDataZkHelper.getConsumerPath(ROOT, serviceName);
        return queryRegistryDataByPath(consumerPath);
    }

    private List<RegistryData> queryRegistryDataByPath(String path) {
        List<String> childrenData = zkClient.getChildrenData(path);
        List<RegistryData> registryDataList = new ArrayList<>();
        for (String data : childrenData) {
            RegistryData registryData = JSON.parseObject(data, RegistryData.class);
            registryDataList.add(registryData);
        }
        return registryDataList;
    }
}
