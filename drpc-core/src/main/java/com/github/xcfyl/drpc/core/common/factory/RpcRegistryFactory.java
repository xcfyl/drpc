package com.github.xcfyl.drpc.core.common.factory;

import com.github.xcfyl.drpc.core.common.enums.RpcRegistryType;
import com.github.xcfyl.drpc.core.registry.RpcRegistry;
import com.github.xcfyl.drpc.core.registry.zookeeper.ZkRegistry;
import com.github.xcfyl.drpc.core.registry.zookeeper.ZkClient;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:24
 */
public class RpcRegistryFactory {
    public static RpcRegistry createRpcRegistry(RpcRegistryType type, String registryAddr) {
        if (type == RpcRegistryType.ZK) {
            ZkClient zookeeperClient = new ZkClient(registryAddr);
            return new ZkRegistry(zookeeperClient);
        }
        throw new RuntimeException("暂不支持的注册中心类型");
    }
}
