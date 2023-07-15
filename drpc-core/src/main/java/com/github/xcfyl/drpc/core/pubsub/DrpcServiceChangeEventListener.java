package com.github.xcfyl.drpc.core.pubsub;

import com.github.xcfyl.drpc.core.client.DrpcConnectionWrapper;
import com.github.xcfyl.drpc.core.client.DprcConnectionManager;
import com.github.xcfyl.drpc.core.client.DrpcClientContext;
import com.github.xcfyl.drpc.core.registry.DrpcProviderData;
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
public class DrpcServiceChangeEventListener implements DrpcEventListener<DrpcServiceChangeEvent> {
    private final DrpcClientContext rpcClientContext;

    public DrpcServiceChangeEventListener(DrpcClientContext rpcClientContext) {
        this.rpcClientContext = rpcClientContext;
    }

    /**
     * 在这里更新client的连接列表
     *
     * @param event
     */
    @Override
    public void callback(DrpcServiceChangeEvent event) {
        DrpcServiceChangeEventData data = event.getData();
        String serviceName = data.getServiceName();
        List<DrpcProviderData> newProviderDataList = data.getNewServiceList();
        DprcConnectionManager connectionManager = rpcClientContext.getConnectionManager();
        List<DrpcConnectionWrapper> connections =connectionManager.getOriginalConnections(serviceName);
        Map<String, DrpcConnectionWrapper> connectionWrapperMap = new HashMap<>();
        for (DrpcConnectionWrapper connectionWrapper : connections) {
            connectionWrapperMap.put(connectionWrapper.toString(), connectionWrapper);
        }
        List<DrpcConnectionWrapper> newConnections = new ArrayList<>();
        for (DrpcProviderData registryData : newProviderDataList) {
            String ip = registryData.getIp();
            Integer port = registryData.getPort();
            DrpcConnectionWrapper connectionWrapper = new DrpcConnectionWrapper();
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
