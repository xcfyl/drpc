package com.github.xcfyl.pandarpc.core.client;


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
    private static Bootstrap bootstrap;
    /**
     * 没有经过过滤器处理的连接缓存
     */
    private static final Map<String, List<ConnectionWrapper>> ORIGINAL_CONNECT_CACHE = new HashMap<>();
    /**
     * 经过过滤器处理的连接缓存
     */
    private static final Map<String, List<ConnectionWrapper>> FILTERED_CONNECT_CACHE = new HashMap<>();

    public static void setBootstrap(Bootstrap bootstrap) {
        ConnectionManager.bootstrap = bootstrap;
    }

    public static void connect(String serviceName, String addr) {
        ConnectionWrapper connectionWrapper = getConnectionWrapper(addr);
        // 缓存当前连接对象
        List<ConnectionWrapper> connectionWrappers =
                ORIGINAL_CONNECT_CACHE.getOrDefault(serviceName, new ArrayList<>());
        connectionWrappers.add(connectionWrapper);
        ORIGINAL_CONNECT_CACHE.put(serviceName, connectionWrappers);
        RpcClientContext.getRouter().refresh(serviceName);
    }

    public static List<ConnectionWrapper> getOriginalConnections(String serviceName) {
        return ORIGINAL_CONNECT_CACHE.get(serviceName);
    }

    public static List<ConnectionWrapper> getFilteredConnections(String serviceName) {
        FILTERED_CONNECT_CACHE.clear();
        FILTERED_CONNECT_CACHE.putAll(ORIGINAL_CONNECT_CACHE);
        return FILTERED_CONNECT_CACHE.get(serviceName);
    }

    public static void setConnections(String serviceName, List<ConnectionWrapper> connectionWrappers) {
        ORIGINAL_CONNECT_CACHE.put(serviceName, connectionWrappers);
    }

    public static ConnectionWrapper getConnectionWrapper(String addr) {
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

    public static ChannelFuture getChannelFuture(String ip, Integer port) {
        try {
            return bootstrap.connect(ip, port).sync();
        } catch (Exception e) {
            log.error("get connection failure ip #{}, port #{}, exception is #{}", ip, port, e.getMessage());
        }
        return null;
    }
}
