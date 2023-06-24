package com.github.xcfyl.pandarpc.core.proxy;

import com.github.xcfyl.pandarpc.core.client.SubscribedServiceWrapper;

/**
 * @author 西城风雨楼
 */
public interface RpcProxy {
    /**
     * 获取代理对象
     *
     * @return
     * @param <T>
     * @throws Exception
     */
    <T> T get(SubscribedServiceWrapper<T> serviceWrapper) throws Exception;
}
