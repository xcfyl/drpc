package com.github.xcfyl.drpc.core.client;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 包装客户端的订阅服务，给客户端订阅的服务增加额外的属性
 *
 * @author 西城风雨楼
 * @date create at 2023/6/24 09:37
 */
public class DrpcServiceWrapper<T> {
    /**
     * 订阅的服务的class
     */
    private Class<T> serviceClass;
    /**
     * 当前服务调用是否是同步调用
     * 如果为true，那么是同步调用，需要等待超时时间
     * 如果为false，那么是异步调用，不需要等待超时时间
     */
    private Boolean isSync;
    /**
     * 如果因为超时导致发送失败，那么尝试的次数
     */
    private Integer retryTimes;
    /**
     * 重复请求的间隔时间
     */
    private Long retryInterval;
    /**
     * 超时时间
     */
    private Long timeout;

    public DrpcServiceWrapper() {
        // 默认情况下只重试3次
        retryTimes = 3;
    }

    public Class<T> getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class<T> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Boolean getSync() {
        return isSync;
    }

    public void setSync(Boolean sync) {
        isSync = sync;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Long getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Long retryInterval) {
        this.retryInterval = retryInterval;
    }

    @Override
    public String toString() {
        return "DrpcServiceWrapper{" +
                "serviceClass=" + serviceClass +
                ", isSync=" + isSync +
                ", retryTimes=" + retryTimes +
                ", retryInterval=" + retryInterval +
                ", timeout=" + timeout +
                '}';
    }
}
