package com.github.xcfyl.pandarpc.core.registry.zookeeper;

import com.alibaba.fastjson.JSON;
import com.github.xcfyl.pandarpc.core.common.enums.RegistryDataAttrName;
import com.github.xcfyl.pandarpc.core.registry.RegistryData;

/**
 * Zookeeper操作Registry的辅助工具类
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 21:51
 */
public class RegistryDataZkHelper {
    /**
     * 从registryData中解析出当前服务的路径
     *
     * @param root
     * @param registryData
     * @return
     */
    public static String getServicePath(String root, RegistryData registryData) {
        return getServicePath(root, registryData.getServiceName());
    }

    public static String getServicePath(String root, String serviceName) {
        return root + "/" + serviceName + "/provider";
    }

    /**
     * 从registryData中解析出某个服务下订阅的消费者列表路径
     *
     * @param root
     * @param registryData
     * @return
     */
    public static String getConsumerPath(String root, RegistryData registryData) {
        return getConsumerPath(root, registryData.getServiceName());
    }

    public static String getConsumerPath(String root, String serviceName) {
        return root + "/" + serviceName + "/consumer";
    }

    /**
     * 将registryData转换为服务提供者节点在zk中的路径
     *
     * @param registryData
     * @return
     */
    public static String getProviderNodePath(String root, RegistryData registryData) {
        return root + "/" + registryData.getServiceName() + "/provider/" + registryData.getIp() + ":" + registryData.getPort();
    }

    /**
     * 将registryData转换为消费者节点在zk中的路径
     *
     * @param registryData
     * @return
     */
    public static String getConsumerNodePath(String root, RegistryData registryData) {
        return root + "/" + registryData.getServiceName() + "/consumer/" + registryData.getApplicationName() + ":" + registryData.getIp();
    }

    /**
     * 将服务提供者的registryData转换为zk中的元数据，即叶子节点的数据
     *
     * @param registryData
     * @return
     */
    public static String getProviderMetaData(RegistryData registryData) {
        return JSON.toJSONString(registryData);
    }

    /**
     * 将消费者的registryData转换为zk中的元数据，即叶子节点的数据
     *
     * @param registryData
     * @return
     */
    public static String getConsumerMetaData(RegistryData registryData) {
        return JSON.toJSONString(registryData);
    }

    public static void main(String[] args) {
        RegistryData registryData = new RegistryData();
        registryData.setServiceName("com.github.xcfyl.drpc.core.service");
        registryData.setIp("localhost");
        registryData.setPort(1234);
        registryData.setApplicationName("drpc-core");
        registryData.getAttr().put(RegistryDataAttrName.TYPE.getDescription(), "consumer");
        System.out.println(JSON.toJSONString(registryData));
    }
}
