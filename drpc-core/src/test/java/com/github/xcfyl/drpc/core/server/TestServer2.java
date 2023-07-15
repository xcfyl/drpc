package com.github.xcfyl.drpc.core.server;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 23:24
 */
@Slf4j
public class TestServer2 {
    public static void main(String[] args) throws Exception {
        DrpcServer rpcServer = new DrpcServer("drpc_server2.properties");
        rpcServer.init();
        rpcServer.registerService(new HelloServiceImpl());
    }
}
