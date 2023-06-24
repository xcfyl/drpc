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
        SubscribedServiceWrapper<HelloService> wrapper = new SubscribedServiceWrapper<>();
        wrapper.setSync(true);
        wrapper.setServiceClass(HelloService.class);
        HelloService helloService = rpcReference.get(wrapper);
        for (int i = 0; i < 1000; i++) {
            String reply = helloService.hello("zhangsan");
            System.out.println(reply);
        }
    }
}
