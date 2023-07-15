package com.github.xcfyl.drpc.core.proxy;

import com.github.xcfyl.drpc.core.client.ServiceWrapper;

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
    <T> T get(ServiceWrapper<T> serviceWrapper) throws Exception;
}
