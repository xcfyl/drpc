package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.common.enums.DrpcProxyType;
import com.github.xcfyl.drpc.core.common.enums.DrpcRegistryType;
import com.github.xcfyl.drpc.core.common.enums.DrpcRouterType;
import com.github.xcfyl.drpc.core.common.enums.DrpcSerializeType;
import lombok.Data;
import lombok.ToString;

/**
 * 存放和rpc客户端相关的配置信息
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:56
 */
public class DrpcClientConfig {
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 最大请求长度
     */
    private Integer maxRequestLength;
    /**
     * 注册中心类型
     */
    private DrpcRegistryType registryType;
    /**
     * 注册中心的地址
     */
    private String registryAddr;
    /**
     * 序列化类型
     */
    private DrpcSerializeType serializeType;
    /**
     * 请求超时时间
     */
    private Long requestTimeout;
    /**
     * 代理类型
     */
    private DrpcProxyType proxyType;
    /**
     * 内置的路由类型
     */
    private DrpcRouterType routerType;
    /**
     * 订阅服务失败后重试的次数
     */
    private Integer subscribeRetryTimes;
    /**
     * 订阅服务失败后重试的间隔
     */
    private Long subscribeRetryInterval;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getMaxRequestLength() {
        return maxRequestLength;
    }

    public void setMaxRequestLength(Integer maxRequestLength) {
        this.maxRequestLength = maxRequestLength;
    }

    public DrpcRegistryType getRegistryType() {
        return registryType;
    }

    public void setRegistryType(DrpcRegistryType registryType) {
        this.registryType = registryType;
    }

    public String getRegistryAddr() {
        return registryAddr;
    }

    public void setRegistryAddr(String registryAddr) {
        this.registryAddr = registryAddr;
    }

    public DrpcSerializeType getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(DrpcSerializeType serializeType) {
        this.serializeType = serializeType;
    }

    public Long getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Long requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public DrpcProxyType getProxyType() {
        return proxyType;
    }

    public void setProxyType(DrpcProxyType proxyType) {
        this.proxyType = proxyType;
    }

    public DrpcRouterType getRouterType() {
        return routerType;
    }

    public void setRouterType(DrpcRouterType routerType) {
        this.routerType = routerType;
    }

    public Integer getSubscribeRetryTimes() {
        return subscribeRetryTimes;
    }

    public void setSubscribeRetryTimes(Integer subscribeRetryTimes) {
        this.subscribeRetryTimes = subscribeRetryTimes;
    }

    public Long getSubscribeRetryInterval() {
        return subscribeRetryInterval;
    }

    public void setSubscribeRetryInterval(Long subscribeRetryInterval) {
        this.subscribeRetryInterval = subscribeRetryInterval;
    }

    @Override
    public String toString() {
        return "DrpcClientConfig{" +
                "applicationName='" + applicationName + '\'' +
                ", maxRequestLength=" + maxRequestLength +
                ", registryType=" + registryType +
                ", registryAddr='" + registryAddr + '\'' +
                ", serializeType=" + serializeType +
                ", requestTimeout=" + requestTimeout +
                ", proxyType=" + proxyType +
                ", routerType=" + routerType +
                ", subscribeRetryTimes=" + subscribeRetryTimes +
                ", subscribeRetryInterval=" + subscribeRetryInterval +
                '}';
    }
}
