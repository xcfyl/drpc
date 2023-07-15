package com.github.xcfyl.drpc.core.registry.zookeeper;

/**
 * Zookeeper操作Registry的辅助工具类
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 21:51
 */
public class ZkPathHelper {
    /**
     * 得到指定服务下面所有的服务提供者的根路径
     */
    public static String getProvidersPath(String root, String serviceName) {
        return root + "/" + serviceName + "/provider";
    }

    public static String getProviderPath(String root, String serviceName, String ip, Integer port) {
        return root + "/" + serviceName + "/provider/" + ip + ":" + port;
    }


    /**
     * 返回某个服务下，所有消费者的根路径
     */
    public static String getConsumersPath(String root, String serviceName) {
        return root + "/" + serviceName + "/consumer";
    }

    /**
     * 将registryData转换为消费者节点在zk中的路径
     */
    public static String getConsumerPath(String root, String serviceName, String applicationName, String ip) {
        return root + "/" + serviceName + "/consumer/" + applicationName + ":" + ip;
    }
}
