package com.github.xcfyl.drpc.core.common.factory;

import com.github.xcfyl.drpc.core.registry.RpcRegistry;
import com.github.xcfyl.drpc.core.registry.zookeeper.ZookeeperRegistry;
import com.github.xcfyl.drpc.core.common.config.RpcCommonConfig;
import com.github.xcfyl.drpc.core.registry.zookeeper.ZookeeperClient;

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
