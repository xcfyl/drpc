package com.github.xcfyl.pandarpc.core.client;

import com.github.xcfyl.pandarpc.core.proxy.ProxyFactory;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/22 12:05
 */
public class RpcReference {
    private final ProxyFactory proxyFactory;

    public RpcReference(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    /**
     * 获取某个接口的代理类对象
     *
     * @param clazz 被代理的类
     * @return 返回clazz的代理类对象
     * @param <T> 返回的代理对象类型
     * @throws Throwable 可能抛出的异常
     */
    public <T> T get(SubscribedServiceWrapper<T> serviceWrapper) throws Throwable {
        return proxyFactory.getProxy(serviceWrapper);
    }
}
