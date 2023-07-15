package com.github.xcfyl.drpc.core.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 连接管理器，用于管理当前客户端和远程所有rpc服务的连接
 *
 * @author 西城风雨楼
 * @date create at 2023/6/23 15:26
 */
@Slf4j
public class ConnectionManager {
    private final Bootstrap bootstrap;
    /**
     * 没有经过过滤器处理的连接缓存
     */
    private final Map<String, List<ConnectionWrapper>> originalConnectionCache = new HashMap<>();

    public ConnectionManager(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public void connect(String serviceName, String addr) {
        ConnectionWrapper connectionWrapper = getConnectionWrapper(addr);
        // 缓存当前连接对象
        List<ConnectionWrapper> connectionWrappers =
                originalConnectionCache.getOrDefault(serviceName, new ArrayList<>());
        connectionWrappers.add(connectionWrapper);
        originalConnectionCache.put(serviceName, connectionWrappers);
    }

    public List<ConnectionWrapper> getOriginalConnections(String serviceName) {
        return originalConnectionCache.getOrDefault(serviceName, new ArrayList<>());
    }

    public void setConnections(String serviceName, List<ConnectionWrapper> connectionWrappers) {
        originalConnectionCache.put(serviceName, connectionWrappers);
    }

    public ConnectionWrapper getConnectionWrapper(String addr) {
        String[] split = addr.split(":");
        String ip = split[0];
        int port = Integer.parseInt(split[1]);
        ChannelFuture channelFuture = null;
        channelFuture = getChannelFuture(ip, port);
        ConnectionWrapper connectionWrapper = new ConnectionWrapper();
        connectionWrapper.setChannelFuture(channelFuture);
        connectionWrapper.setIp(ip);
        connectionWrapper.setPort(port);
        return connectionWrapper;
    }

    public ChannelFuture getChannelFuture(String ip, Integer port) {
        try {
            return bootstrap.connect(ip, port).sync();
        } catch (Exception e) {
            log.error("get connection failure ip #{}, port #{}, exception is #{}", ip, port, e.getMessage());
        }
        return null;
    }
}
