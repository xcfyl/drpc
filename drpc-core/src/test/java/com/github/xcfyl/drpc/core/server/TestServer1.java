package com.github.xcfyl.drpc.core.server;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 23:24
 */
@Slf4j
public class TestServer1 {
    public static void main(String[] args) throws Exception {
        RpcServer rpcServer = new RpcServer("drpc_server1.properties");
        rpcServer.init();
        rpcServer.registerService(new HelloServiceImpl());
    }
}
