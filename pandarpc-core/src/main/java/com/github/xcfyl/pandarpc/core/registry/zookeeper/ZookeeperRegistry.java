package com.github.xcfyl.pandarpc.core.registry.zookeeper;

import com.alibaba.fastjson.JSON;
import com.github.xcfyl.pandarpc.core.client.RpcClientContext;
import com.github.xcfyl.pandarpc.core.event.RpcEventPublisher;
import com.github.xcfyl.pandarpc.core.event.RpcServiceUpdateEvent;
import com.github.xcfyl.pandarpc.core.event.data.ServiceUpdateEventData;
import com.github.xcfyl.pandarpc.core.registry.RegistryData;
import com.github.xcfyl.pandarpc.core.registry.RpcRegistry;
import com.github.xcfyl.pandarpc.core.server.RpcServerContext;

import java.util.ArrayList;
import java.util.List;

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
        zkClient.start();
    }

    @Override
    public void register(RegistryData registryData) throws Exception {
        if (!zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String providerMetaData = RegistryDataZkHelper.getProviderMetaData(registryData);
        String providerNodePath = RegistryDataZkHelper.getProviderNodePath(ROOT, registryData);
        if (zkClient.existNode(providerNodePath)) {
            zkClient.deleteNode(providerNodePath);
        }
        zkClient.createTemporaryData(providerNodePath, providerMetaData);
        RpcServerContext.getRegistryDataCache().put(registryData.getApplicationName(), registryData);
    }

    @Override
    public void unregister(RegistryData registryData) throws Exception {
        String providerNodePath = RegistryDataZkHelper.getProviderNodePath(ROOT, registryData);
        RpcServerContext.getRegistryDataCache().remove(registryData.getServiceName());
        zkClient.deleteNode(providerNodePath);
    }

    @Override
    public void subscribe(RegistryData registryData) throws Exception {
        if (!zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String consumerMetaData = RegistryDataZkHelper.getConsumerMetaData(registryData);
        String consumerNodePath = RegistryDataZkHelper.getConsumerNodePath(ROOT, registryData);
        if (zkClient.existNode(consumerNodePath)) {
            zkClient.deleteNode(consumerNodePath);
        }
        zkClient.createTemporaryData(consumerNodePath, consumerMetaData);
        watchServiceChange(registryData);
        RpcClientContext.getRegistryDataCache().put(registryData.getApplicationName(), registryData);
    }

    @Override
    public void unsubscribe(RegistryData registryData) throws Exception {
        String consumerNodePath = RegistryDataZkHelper.getConsumerNodePath(ROOT, registryData);
        zkClient.deleteNode(consumerNodePath);
    }

    @Override
    public List<RegistryData> queryProviders(String serviceName) throws Exception {
        String servicePath = RegistryDataZkHelper.getServicePath(ROOT, serviceName);
        return queryRegistryDataByServicePath(servicePath);
    }

    @Override
    public List<RegistryData> queryConsumers(String serviceName) throws Exception {
        String consumerPath = RegistryDataZkHelper.getConsumerPath(ROOT, serviceName);
        return queryRegistryDataByServicePath(consumerPath);
    }

    @Override
    public void start() throws Exception {
        zkClient.start();
    }

    @Override
    public void close() throws Exception {
        zkClient.destroy();
    }

    /**
     * 获取某个服务下面所有注册的服务提供者的信息
     *
     * @param servicePath
     * @return
     */
    private List<RegistryData> queryRegistryDataByServicePath(String servicePath) throws Exception {
        List<String> providerAddrList = zkClient.getChildrenPaths(servicePath);
        List<RegistryData> registryDataList = new ArrayList<>();
        for (String addr : providerAddrList) {
            String providerPath = servicePath + "/" + addr;
            String providerData = zkClient.getNodeData(providerPath);
            RegistryData registryData = JSON.parseObject(providerData, RegistryData.class);
            registryDataList.add(registryData);
        }
        return registryDataList;
    }

    private void watchServiceChange(RegistryData registryData) throws Exception {
        String servicePath = RegistryDataZkHelper.getServicePath(ROOT, registryData.getServiceName());
        String consumerNodePath = RegistryDataZkHelper.getConsumerNodePath(ROOT, registryData);
        zkClient.watchChildNodeData(servicePath, watchedEvent -> {
            try {
                if (zkClient.existNode(consumerNodePath)) {
                    // 某个消费者取消订阅之后，就会删除自己的节点信息，因此这里不再进行事件触发，并且不再进行下一次订阅
                    List<RegistryData> registryDataList = queryRegistryDataByServicePath(servicePath);
                    ServiceUpdateEventData updateEventData = new ServiceUpdateEventData();
                    updateEventData.setServiceName(registryData.getServiceName());
                    updateEventData.setNewServiceList(registryDataList);
                    RpcServiceUpdateEvent updateEvent = new RpcServiceUpdateEvent();
                    updateEvent.setData(updateEventData);
                    RpcEventPublisher eventPublisher = RpcEventPublisher.getInstance();
                    eventPublisher.publishEvent(updateEvent);
                    watchServiceChange(registryData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
