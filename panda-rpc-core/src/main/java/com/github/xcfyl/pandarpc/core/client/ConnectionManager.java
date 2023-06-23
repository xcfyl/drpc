package com.github.xcfyl.pandarpc.core.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;

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
public class ConnectionManager {
    private static Bootstrap bootstrap;
    private static final Map<String, List<ConnectionWrapper>> CONNECT_CACHE = new HashMap<>();

    public static void setBootstrap(Bootstrap bootstrap) {
        ConnectionManager.bootstrap = bootstrap;
    }

    /**
     * 连接给定的服务下面的所有的服务提供者
     *
     * @param serviceName 当前连接的服务
     * @param addrList 当前服务下面所有服务提供者的访问地址ip:port格式
     */
    public static void connect(String serviceName, List<String> addrList) {
        for (String addr : addrList) {
            connect(serviceName, addr);
        }
    }

    public static void connect(String serviceName, String addr) {
        ConnectionWrapper connectionWrapper = getConnectionWrapper(addr);
        // 缓存当前连接对象
        List<ConnectionWrapper> connectionWrappers =
                CONNECT_CACHE.getOrDefault(serviceName, new ArrayList<>());
        connectionWrappers.add(connectionWrapper);
        CONNECT_CACHE.put(serviceName, connectionWrappers);
        RpcRouterRef.getRpcRouter().refresh(serviceName);
    }

    public static List<ConnectionWrapper> getConnections(String serviceName) {
        return CONNECT_CACHE.get(serviceName);
    }

    public static void setConnections(String serviceName, List<ConnectionWrapper> connectionWrappers) {
        CONNECT_CACHE.put(serviceName, connectionWrappers);
    }

    public static ConnectionWrapper getConnectionWrapper(String addr) {
        String[] split = addr.split(":");
        String ip = split[0];
        int port = Integer.parseInt(split[1]);
        ChannelFuture channelFuture = getChannelFuture(ip, port);
        ConnectionWrapper connectionWrapper = new ConnectionWrapper();
        connectionWrapper.setChannelFuture(channelFuture);
        connectionWrapper.setIp(ip);
        connectionWrapper.setPort(port);
        return connectionWrapper;
    }

    public static ChannelFuture getChannelFuture(String ip, Integer port) {
        return bootstrap.connect(ip, port);
    }
}
