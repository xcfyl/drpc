package com.github.xcfyl.drpc.core.proxy;

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
    <T> T getProxy(Class<T> clazz) throws Throwable;
}
