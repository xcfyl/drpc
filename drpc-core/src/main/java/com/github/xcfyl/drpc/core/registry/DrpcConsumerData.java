package com.github.xcfyl.drpc.core.registry;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者向注册中心写入的数据
 *
 * @author 西城风雨楼
 * @date create at 2023/7/15 13:24
 */
public class DrpcConsumerData {
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 机器的ip地址
     */
    private String ip;
    /**
     * 所监听的端口
     */
    private Integer port;
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 注册数据的扩展数据
     */
    private final Map<String, Object> attrs;

    public DrpcConsumerData() {
        attrs = new HashMap<>();
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setAttr(String key, Object attr) {
        attrs.put(key, attr);
    }

    public Object getAttr(String key) {
        return attrs.get(key);
    }
}
