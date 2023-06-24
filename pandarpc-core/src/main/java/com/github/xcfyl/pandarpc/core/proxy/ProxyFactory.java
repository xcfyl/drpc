package com.github.xcfyl.pandarpc.core.proxy;

import com.github.xcfyl.pandarpc.core.client.SubscribedServiceWrapper;

/**
 * @author 西城风雨楼
 */
public interface ProxyFactory {
    /**
     * 获取代理对象
     *
     * @param clazz
     * @return
     * @param <T>
     * @throws Throwable
     */
    <T> T getProxy(SubscribedServiceWrapper<T> serviceWrapper) throws Throwable;
}
