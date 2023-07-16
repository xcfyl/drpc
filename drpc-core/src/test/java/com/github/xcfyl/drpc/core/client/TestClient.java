package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.server.HelloService;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
        AtomicLong atomicLong = new AtomicLong(0);
        CountDownLatch latch = new CountDownLatch(15);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 15; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    Random random = new Random();
                    int len = random.nextInt(20);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int k = 0; k < len; k++) {
                        stringBuilder.append("a");
                    }
                    Integer reply = helloService.hello(stringBuilder.toString());
                    atomicLong.addAndGet(reply);
                }
                latch.countDown();
            }).start();
        }
        System.out.println("等待执行结束");
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("总共花了: " + (end - start) + "ms");
        System.out.println("总共执行: " + atomicLong.get());
    }
}
