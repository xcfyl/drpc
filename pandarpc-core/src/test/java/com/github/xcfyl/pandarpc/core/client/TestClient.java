package com.github.xcfyl.pandarpc.core.client;

import com.github.xcfyl.pandarpc.core.server.HelloService;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 23:23
 */
public class TestClient {
    public static void main(String[] args) throws Throwable {
        RpcClient rpcClient = new RpcClient();
        RpcReference rpcReference = rpcClient.init();
        rpcClient.subscribeService(HelloService.class.getName());
        HelloService helloService = rpcReference.get(HelloService.class);
        String reply = helloService.hello("zhangsan");
        System.out.println(reply);
    }
}
