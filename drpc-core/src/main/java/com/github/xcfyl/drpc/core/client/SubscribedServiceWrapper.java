package com.github.xcfyl.drpc.core.client;

import lombok.Data;

/**
 * 包装客户端的订阅服务，给客户端订阅的服务增加额外的属性
 *
 * @author 西城风雨楼
 * @date create at 2023/6/24 09:37
 */
@Data
public class SubscribedServiceWrapper<T> {
    /**
     * 订阅的服务的class
     */
    private Class<T> serviceClass;
    /**
     * 当前服务调用是否是同步调用
     * 如果为true，那么是同步调用，需要等待超时时间
     * 如果为false，那么是异步调用，不需要等待超时时间
     */
    private boolean isSync;
}
