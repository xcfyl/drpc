package com.github.xcfyl.drpc.core.common.config;

import org.junit.Test;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 23:20
 */
public class TestConfigLoader {
    @Test
    public void testConfigLoader() {
        RpcClientConfig rpcClientConfig = RpcConfigLoader.loadRpcClientConfig();
        RpcServerConfig rpcServerConfig = RpcConfigLoader.loadRpcServerConfig();
        System.out.println(rpcServerConfig);
        System.out.println(rpcClientConfig);
    }
}
