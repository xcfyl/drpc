package com.github.xcfyl.pandarpc.core.common.factory;

import com.github.xcfyl.pandarpc.core.common.config.RpcCommonConfig;
import com.github.xcfyl.pandarpc.core.registry.RpcRegistry;
import com.github.xcfyl.pandarpc.core.registry.zookeeper.ZookeeperClient;
import com.github.xcfyl.pandarpc.core.registry.zookeeper.ZookeeperRegistry;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:24
 */
public class RpcRegistryFactory {
    public static RpcRegistry createRpcRegistry(RpcCommonConfig config) {
        ZookeeperClient zookeeperClient = new ZookeeperClient(config.getRegistryAddr());
        return new ZookeeperRegistry(zookeeperClient);
    }
}
