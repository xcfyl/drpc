package com.github.xcfyl.drpc.core.pubsub.listener;

import com.github.xcfyl.drpc.core.client.ConnectionWrapper;
import com.github.xcfyl.drpc.core.client.ConnectionManager;
import com.github.xcfyl.drpc.core.client.RpcClientContext;
import com.github.xcfyl.drpc.core.pubsub.event.RpcServiceUpdateEvent;
import com.github.xcfyl.drpc.core.pubsub.event.ServiceUpdateEventData;
import com.github.xcfyl.drpc.core.registry.ProviderRegistryData;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 如果rpc服务列表发生变化，那么会执行该事件监听器的逻辑
 * 具体来说，需要更新本地客户端服务列表的缓存
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 23:30
 */
@Slf4j
public class ServiceUpdateEventListener implements RpcEventListener<RpcServiceUpdateEvent> {
    private final RpcClientContext rpcClientContext;

    public ServiceUpdateEventListener(RpcClientContext rpcClientContext) {
        this.rpcClientContext = rpcClientContext;
    }

    /**
     * 在这里更新client的连接列表
     *
     * @param event
     */
    @Override
    public void callback(RpcServiceUpdateEvent event) {
        ServiceUpdateEventData data = event.getData();
        String serviceName = data.getServiceName();
        List<ProviderRegistryData> newProviderDataList = data.getNewServiceList();
        ConnectionManager connectionManager = rpcClientContext.getConnectionManager();
        List<ConnectionWrapper> connections =connectionManager.getOriginalConnections(serviceName);
        Map<String, ConnectionWrapper> connectionWrapperMap = new HashMap<>();
        for (ConnectionWrapper connectionWrapper : connections) {
            connectionWrapperMap.put(connectionWrapper.toString(), connectionWrapper);
        }
        List<ConnectionWrapper> newConnections = new ArrayList<>();
        for (ProviderRegistryData registryData : newProviderDataList) {
            String ip = registryData.getIp();
            Integer port = registryData.getPort();
            ConnectionWrapper connectionWrapper = new ConnectionWrapper();
            connectionWrapper.setIp(ip);
            connectionWrapper.setPort(port);
            if (!connectionWrapperMap.containsKey(connectionWrapper.toString())) {
                ChannelFuture channelFuture = connectionManager.getChannelFuture(ip, port);
                connectionWrapper.setChannelFuture(channelFuture);
            }
            newConnections.add(connectionWrapper);
        }
        // 更新本地连接缓存
        connectionManager.setConnections(serviceName, newConnections);
        // 刷新路由
        rpcClientContext.getRouter().refresh(serviceName);
    }
}
