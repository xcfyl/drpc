package com.github.xcfyl.drpc.core.common.factory;

import com.github.xcfyl.drpc.core.registry.RpcRegistry;
import com.github.xcfyl.drpc.core.registry.zookeeper.ZkRegistry;
import com.github.xcfyl.drpc.core.common.config.RpcCommonConfig;
import com.github.xcfyl.drpc.core.registry.zookeeper.ZkClient;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:24
 */
public class RpcRegistryFactory {
    public static RpcRegistry createRpcRegistry(RpcCommonConfig config) {
        ZkClient zookeeperClient = new ZkClient(config.getRegistryAddr());
        return new ZkRegistry(zookeeperClient);
    }
}
