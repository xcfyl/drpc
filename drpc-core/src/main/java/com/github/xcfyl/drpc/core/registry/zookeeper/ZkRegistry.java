package com.github.xcfyl.drpc.core.registry.zookeeper;

import com.alibaba.fastjson.JSON;
import com.github.xcfyl.drpc.core.pubsub.RpcEventPublisher;
import com.github.xcfyl.drpc.core.pubsub.event.ServiceUpdateEvent;
import com.github.xcfyl.drpc.core.pubsub.event.ServiceUpdateEventData;
import com.github.xcfyl.drpc.core.registry.ConsumerData;
import com.github.xcfyl.drpc.core.registry.ProviderData;
import com.github.xcfyl.drpc.core.registry.Registry;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于zookeeper实现的注册中心
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 20:54
 */
public class ZkRegistry implements Registry {
    private static final String ROOT = "/drpc";
    private final ZkClient zkClient;

    public ZkRegistry(ZkClient zkClient) {
        this.zkClient = zkClient;
        zkClient.start();
    }

    @Override
    public void register(ProviderData registryData) throws Exception {
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
    public void unregister(ProviderData registryData) throws Exception {
        String providerNodePath = ZkPathHelper.getProviderPath(ROOT, registryData.getApplicationName(), registryData.getIp(), registryData.getPort());
        zkClient.deleteNode(providerNodePath);
    }

    @Override
    public void subscribe(ConsumerData registryData) throws Exception {
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
    public void unsubscribe(ConsumerData registryData) throws Exception {
        String consumerNodePath = ZkPathHelper.getConsumerPath(ROOT, registryData.getServiceName(), registryData.getApplicationName(), registryData.getIp());
        zkClient.deleteNode(consumerNodePath);
    }

    @Override
    public List<ProviderData> queryProviders(String serviceName) throws Exception {
        String servicePath = ZkPathHelper.getProvidersPath(ROOT, serviceName);
        return queryProviderRegistryData(servicePath);
    }

    @Override
    public List<ConsumerData> queryConsumers(String serviceName) throws Exception {
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
    private List<ConsumerData> queryConsumerRegistryData(String servicePath) throws Exception {
        List<String> paths = zkClient.getChildrenPaths(servicePath);
        List<ConsumerData> registryDataList = new ArrayList<>();
        for (String addr : paths) {
            String providerPath = servicePath + "/" + addr;
            String data = zkClient.getNodeData(providerPath);
            ConsumerData registryData = JSON.parseObject(data, ConsumerData.class);
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
    private List<ProviderData> queryProviderRegistryData(String servicePath) throws Exception {
        List<String> paths = zkClient.getChildrenPaths(servicePath);
        List<ProviderData> registryDataList  = new ArrayList<>();
        for (String addr : paths) {
            String providerPath = servicePath + "/" + addr;
            String data = zkClient.getNodeData(providerPath);
            ProviderData registryData = JSON.parseObject(data, ProviderData.class);
            registryDataList.add(registryData);
        }
        return registryDataList;
    }

    private void watchServiceChange(ConsumerData registryData) throws Exception {
        String servicePath = ZkPathHelper.getProvidersPath(ROOT, registryData.getServiceName());
        String consumerNodePath = ZkPathHelper.getConsumerPath(ROOT, registryData.getServiceName(), registryData.getApplicationName(), registryData.getIp());
        zkClient.watchChildNodeData(servicePath, watchedEvent -> {
            try {
                if (zkClient.existNode(consumerNodePath)) {
                    // 某个消费者取消订阅之后，就会删除自己的节点信息，因此这里不再进行事件触发，并且不再进行下一次订阅
                    List<ProviderData> registryDataList = queryProviderRegistryData(servicePath);
                    ServiceUpdateEventData updateEventData = new ServiceUpdateEventData();
                    updateEventData.setServiceName(registryData.getServiceName());
                    updateEventData.setNewServiceList(registryDataList);
                    ServiceUpdateEvent updateEvent = new ServiceUpdateEvent();
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
