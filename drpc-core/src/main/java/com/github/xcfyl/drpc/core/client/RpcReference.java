package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.proxy.RpcProxy;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/22 12:05
 */
public class RpcReference {
    private final RpcProxy proxyFactory;

    public RpcReference(RpcProxy proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    /**
     * 获取某个接口的代理类对象
     *
     * @param serviceWrapper 包装了代理对象的对象
     * @return 返回clazz的代理类对象
     * @param <T> 返回的代理对象类型
     * @throws Throwable 可能抛出的异常
     */
    public <T> T get(SubscribedServiceWrapper<T> serviceWrapper) throws Throwable {
        return proxyFactory.get(serviceWrapper);
    }
}
