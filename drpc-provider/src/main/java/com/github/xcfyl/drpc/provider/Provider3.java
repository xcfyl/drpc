package com.github.xcfyl.drpc.provider;

import com.github.xcfyl.drpc.api.impl.HelloServiceImpl;
import com.github.xcfyl.drpc.core.server.DrpcServer;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 14:35
 */
public class Provider3 {
    public static void main(String[] args) throws Exception {
        DrpcServer rpcServer = new DrpcServer("drpc_server3.properties");
        rpcServer.init();
        rpcServer.registerService(new HelloServiceImpl());
    }
}
