package com.github.xcfyl.drpc.core.registry;

import java.util.List;

/**
 * rpc注册中心接口
 *
 * @author 西城风雨楼
 */
public interface RpcRegistry {
    /**
     * 将某个服务信息写入注册中心
     *
     * @param registryData
     * @return
     */
    boolean register(RegistryData registryData);

    /**
     * 将某个服务信息从注册中心删除
     *
     * @param registryData
     * @return
     */
    boolean unregister(RegistryData registryData);

    /**
     * 订阅注册中心中某个服务的数据，后续有变更的时候，可以及时获得感知
     *
     * @param registryData
     * @return
     */
    boolean subscribe(RegistryData registryData);

    /**
     * 取消订阅注册中心某个服务的数据，后续将不再关注数据的变化
     *
     * @param
     * @return
     */
    boolean unsubscribe(RegistryData serviceName);

    /**
     * 查询某个服务下面所有服务提供者的信息
     *
     * @param serviceName
     * @return
     */
    List<RegistryData> queryProviders(String serviceName);

    /**
     * 查询某个服务下面所有消费者的信息
     *
     * @param serviceName
     * @return
     */
    List<RegistryData> queryConsumers(String serviceName);
}
