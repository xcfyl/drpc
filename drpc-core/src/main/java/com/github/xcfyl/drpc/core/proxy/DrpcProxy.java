package com.github.xcfyl.drpc.core.proxy;

import com.github.xcfyl.drpc.core.client.DrpcServiceWrapper;

/**
 * @author 西城风雨楼
 */
public interface DrpcProxy {
    /**
     * 获取代理对象
     *
     * @return
     * @param <T>
     * @throws Exception
     */
    <T> T get(DrpcServiceWrapper<T> serviceWrapper) throws Exception;
}
