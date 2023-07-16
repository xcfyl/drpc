package com.github.xcfyl.drpc.core.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class DprcConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(DprcConnectionManager.class);

    private final Bootstrap bootstrap;
    /**
     * 没有经过过滤器处理的连接缓存
     */
    private final Map<String, List<DrpcConnectionWrapper>> originalConnectionCache = new HashMap<>();

    public DprcConnectionManager(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public void connect(String serviceName, String addr) {
        DrpcConnectionWrapper connectionWrapper = getConnectionWrapper(addr);
        // 缓存当前连接对象
        List<DrpcConnectionWrapper> connectionWrappers =
                originalConnectionCache.getOrDefault(serviceName, new ArrayList<>());
        connectionWrappers.add(connectionWrapper);
        originalConnectionCache.put(serviceName, connectionWrappers);
    }

    public List<DrpcConnectionWrapper> getOriginalConnections(String serviceName) {
        return originalConnectionCache.getOrDefault(serviceName, new ArrayList<>());
    }

    public void setConnections(String serviceName, List<DrpcConnectionWrapper> connectionWrappers) {
        originalConnectionCache.put(serviceName, connectionWrappers);
    }

    public DrpcConnectionWrapper getConnectionWrapper(String addr) {
        String[] split = addr.split(":");
        String ip = split[0];
        int port = Integer.parseInt(split[1]);
        ChannelFuture channelFuture = getChannelFuture(ip, port);
        DrpcConnectionWrapper connectionWrapper = new DrpcConnectionWrapper();
        connectionWrapper.setChannelFuture(channelFuture);
        connectionWrapper.setIp(ip);
        connectionWrapper.setPort(port);
        return connectionWrapper;
    }

    public ChannelFuture getChannelFuture(String ip, Integer port) {
        try {
            return bootstrap.connect(ip, port).sync();
        } catch (Exception e) {
            logger.error("get connection failure ip {}, port {}, exception is {}", ip, port, e.getMessage());
        }
        return null;
    }
}
