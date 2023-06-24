package com.github.xcfyl.drpc.core.registry;

import com.github.xcfyl.drpc.core.common.enums.RpcRegistryDataAttrName;
import com.github.xcfyl.drpc.core.pubsub.RpcEventPublisher;
import com.github.xcfyl.drpc.core.pubsub.listener.ServiceUpdateEventListener;
import com.github.xcfyl.drpc.core.registry.zookeeper.ZookeeperClient;
import com.github.xcfyl.drpc.core.registry.zookeeper.ZookeeperRegistry;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 编写和zookeeper注册中心相关的测试
 *
 * @author 西城风雨楼
 * @date create at 2023/6/23 10:16
 */
@Slf4j
public class TestZookeeperRegistry {
    private ZookeeperRegistry zookeeperRegistry;
    private RpcEventPublisher rpcEventPublisher;

    @Before
    public void init() {
        rpcEventPublisher = new RpcEventPublisher();
        zookeeperRegistry = new ZookeeperRegistry(new ZookeeperClient("127.0.0.1:2181"));
    }

    /**
     * 测试通过注册中心注册数据的
     */
    @Test
    public void testRegistry() throws Exception {
        // 创建注册数据
        RegistryData provider1 = new RegistryData();
        provider1.setIp("localhost");
        provider1.setPort(1234);
        provider1.setServiceName("service1");
        provider1.setApplicationName("app1");
        provider1.getAttr().put(RpcRegistryDataAttrName.TYPE.getDescription(), "provider");

        RegistryData provider2 = new RegistryData();
        provider2.setIp("localhost");
        provider2.setPort(1235);
        provider2.setServiceName("service1");
        provider2.setApplicationName("app2");

        RegistryData provider3 = new RegistryData();
        provider3.setIp("localhost");
        provider3.setPort(1236);
        provider3.setServiceName("service1");
        provider3.setApplicationName("app3");

        // 添加服务变化监听器
        rpcEventPublisher.addEventListener(new ServiceUpdateEventListener());

        // 注册provider1
        zookeeperRegistry.register(provider1);
        // 注册provider2
        zookeeperRegistry.register(provider2);
        // 注册provider3
        zookeeperRegistry.register(provider3);
        // 看注册是否成功
        Assert.assertEquals("注册服务测试不通过", 3, zookeeperRegistry.queryProviders("service1").size());

        RegistryData consumer1 = new RegistryData();
        consumer1.setServiceName("service1");
        consumer1.setIp("localhost");
        consumer1.setApplicationName("consumer1");
        // 让消费者1订阅服务1
        zookeeperRegistry.subscribe(consumer1);
        Assert.assertEquals("测试服务订阅不通过", 1, zookeeperRegistry.queryConsumers("service1").size());

        // 取消注册provider1，观察服务变更事件监听器是否触发
        // 这里会触发一次
        zookeeperRegistry.unregister(provider1);
        Assert.assertEquals("测试取消注册不通过", 2, zookeeperRegistry.queryProviders("service1").size());

        Thread.sleep(30);

        // 取消注册provider2，观察服务变更事件监听器是否触发
        // 目的是为了检验能够监听到多次变化
        // 这里会触发一次
        zookeeperRegistry.unregister(provider2);
        Assert.assertEquals("测试取消注册不通过", 1, zookeeperRegistry.queryProviders("service1").size());

        // 尝试取消订阅
        zookeeperRegistry.unsubscribe(consumer1);
        Assert.assertEquals("取消订阅不通过", 0, zookeeperRegistry.queryConsumers("service1").size());
        Thread.sleep(30);

        // 测试取消注册后，还能不能订阅监听事件，如果观察到输出，那么说明有问题
        // 这里不会触发
        zookeeperRegistry.register(provider1);
        Thread.sleep(30);
        log.debug("测试完毕");
    }
}
