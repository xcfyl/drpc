package com.github.xcfyl.drpc.core.registry.zookeeper;

import com.alibaba.fastjson.JSON;
import com.github.xcfyl.drpc.core.pubsub.RpcEventPublisher;
import com.github.xcfyl.drpc.core.pubsub.event.RpcServiceUpdateEvent;
import com.github.xcfyl.drpc.core.pubsub.event.ServiceUpdateEventData;
import com.github.xcfyl.drpc.core.registry.ConsumerRegistryData;
import com.github.xcfyl.drpc.core.registry.ProviderRegistryData;
import com.github.xcfyl.drpc.core.registry.RpcRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于zookeeper实现的注册中心
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 20:54
 */
public class ZkRegistry implements RpcRegistry {
    private static final String ROOT = "/drpc";
    private final ZkClient zkClient;

    public ZkRegistry(ZkClient zkClient) {
        this.zkClient = zkClient;
        zkClient.start();
    }

    @Override
    public void register(ProviderRegistryData registryData) throws Exception {
        if (!zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        // 服务提供者的数据
        String providerMetaData = JSON.toJSONString(registryData);
        // 服务提供者的节点路径
        String providerNodePath = ZkPathHelper.getProviderPath(ROOT, registryData.getServiceName(), registryData.getIp(), registryData.getPort());
        if (zkClient.existNode(providerNodePath)) {
            zkClient.deleteNode(providerNodePath);
        }
        zkClient.createTemporaryData(providerNodePath, providerMetaData);
    }

    @Override
    public void unregister(ProviderRegistryData registryData) throws Exception {
        String providerNodePath = ZkPathHelper.getProviderPath(ROOT, registryData.getApplicationName(), registryData.getIp(), registryData.getPort());
        zkClient.deleteNode(providerNodePath);
    }

    @Override
    public void subscribe(ConsumerRegistryData registryData) throws Exception {
        if (!zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String consumerMetaData = JSON.toJSONString(registryData);
        String consumerNodePath = ZkPathHelper.getConsumerPath(ROOT, registryData.getServiceName(), registryData.getApplicationName(), registryData.getIp());
        if (zkClient.existNode(consumerNodePath)) {
            zkClient.deleteNode(consumerNodePath);
        }
        zkClient.createTemporaryData(consumerNodePath, consumerMetaData);
        watchServiceChange(registryData);
    }

    @Override
    public void unsubscribe(ConsumerRegistryData registryData) throws Exception {
        String consumerNodePath = ZkPathHelper.getConsumerPath(ROOT, registryData.getServiceName(), registryData.getApplicationName(), registryData.getIp());
        zkClient.deleteNode(consumerNodePath);
    }

    @Override
    public List<ProviderRegistryData> queryProviders(String serviceName) throws Exception {
        String servicePath = ZkPathHelper.getProvidersPath(ROOT, serviceName);
        return queryProviderRegistryData(servicePath);
    }

    @Override
    public List<ConsumerRegistryData> queryConsumers(String serviceName) throws Exception {
        String consumerPath = ZkPathHelper.getConsumersPath(ROOT, serviceName);
        return queryConsumerRegistryData(consumerPath);
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
    private List<ConsumerRegistryData> queryConsumerRegistryData(String servicePath) throws Exception {
        List<String> paths = zkClient.getChildrenPaths(servicePath);
        List<ConsumerRegistryData> registryDataList = new ArrayList<>();
        for (String addr : paths) {
            String providerPath = servicePath + "/" + addr;
            String data = zkClient.getNodeData(providerPath);
            ConsumerRegistryData registryData = JSON.parseObject(data, ConsumerRegistryData.class);
            registryDataList.add(registryData);
        }
        return registryDataList;
    }

    /**
     * 获取某个服务下面所有注册的服务提供者的信息
     *
     * @param servicePath
     * @return
     */
    private List<ProviderRegistryData> queryProviderRegistryData(String servicePath) throws Exception {
        List<String> paths = zkClient.getChildrenPaths(servicePath);
        List<ProviderRegistryData> registryDataList  = new ArrayList<>();
        for (String addr : paths) {
            String providerPath = servicePath + "/" + addr;
            String data = zkClient.getNodeData(providerPath);
            ProviderRegistryData registryData = JSON.parseObject(data, ProviderRegistryData.class);
            registryDataList.add(registryData);
        }
        return registryDataList;
    }

    private void watchServiceChange(ConsumerRegistryData registryData) throws Exception {
        String servicePath = ZkPathHelper.getProvidersPath(ROOT, registryData.getServiceName());
        String consumerNodePath = ZkPathHelper.getConsumerPath(ROOT, registryData.getServiceName(), registryData.getApplicationName(), registryData.getIp());
        zkClient.watchChildNodeData(servicePath, watchedEvent -> {
            try {
                if (zkClient.existNode(consumerNodePath)) {
                    // 某个消费者取消订阅之后，就会删除自己的节点信息，因此这里不再进行事件触发，并且不再进行下一次订阅
                    List<ProviderRegistryData> registryDataList = queryProviderRegistryData(servicePath);
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
