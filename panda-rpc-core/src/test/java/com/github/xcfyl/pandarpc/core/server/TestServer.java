package com.github.xcfyl.pandarpc.core.server;

import org.junit.Test;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 23:24
 */
public class TestServer {
    @Test
    public void testServer() throws Exception {
        RpcServer rpcServer = new RpcServer();
        rpcServer.init();
        rpcServer.registerService(new HelloServiceImpl());
    }

    public static void main(String[] args) throws Exception {
        RpcServer rpcServer = new RpcServer();
        rpcServer.init();
        rpcServer.registerService(new HelloServiceImpl());
    }
}
