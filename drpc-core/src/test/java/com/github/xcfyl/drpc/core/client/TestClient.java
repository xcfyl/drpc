package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.server.HelloService;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 23:23
 */
public class TestClient {
    public static void main(String[] args) throws Throwable {
        DrpcClient rpcClient = new DrpcClient("drpc_client.properties");
        DrpcRemoteReference rpcReference = rpcClient.init();
        rpcClient.subscribeService(HelloService.class.getName());
        DrpcServiceWrapper<HelloService> wrapper = new DrpcServiceWrapper<>();
        wrapper.setSync(true);
        wrapper.setServiceClass(HelloService.class);
        HelloService helloService = rpcReference.get(wrapper);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String reply = helloService.hello("zhangsan" + i);
            System.out.println(reply);
        }
        long end = System.currentTimeMillis();
        System.out.println("总共花了: " + (end - start) + "ms");
    }
}
