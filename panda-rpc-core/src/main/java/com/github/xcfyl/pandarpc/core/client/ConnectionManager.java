package com.github.xcfyl.pandarpc.core.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接管理器，用于管理当前客户端和远程所有rpc服务的连接
 *
 * @author 西城风雨楼
 * @date create at 2023/6/23 15:26
 */
public class ConnectionManager {
    private final Map<String, ConnectionWrapper> connectionWrapperMap = new ConcurrentHashMap<>();

    /**
     * 连接给定的服务下面的所有的服务提供者
     *
     * @param serviceName
     */
    public void connect(String serviceName) {

    }
}
