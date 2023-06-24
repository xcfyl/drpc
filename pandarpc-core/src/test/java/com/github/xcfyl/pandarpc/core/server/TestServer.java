package com.github.xcfyl.pandarpc.core.server;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 23:24
 */
@Slf4j
public class TestServer {
    public static void main(String[] args) throws Exception {
        RpcServer rpcServer = new RpcServer();
        rpcServer.init();
        rpcServer.registerService(new HelloServiceImpl());
        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("over");
            System.exit(0);
        }).start();
    }
}
