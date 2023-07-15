package com.github.xcfyl.drpc.core.proxy.jdk;

import com.github.xcfyl.drpc.core.client.DrpcClientContext;
import com.github.xcfyl.drpc.core.client.DrpcServiceWrapper;
import com.github.xcfyl.drpc.core.proxy.DrpcProxy;

import java.lang.reflect.Proxy;

/**
 * 基于jdk动态代理模式的代理工厂
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 11:16
 */
public class DrpcJdkProxy implements DrpcProxy {
    private final DrpcClientContext rpcClientContext;

    public DrpcJdkProxy(DrpcClientContext rpcClientContext) {
        this.rpcClientContext = rpcClientContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(DrpcServiceWrapper<T> serviceWrapper) throws Exception {
        return (T) Proxy.newProxyInstance(serviceWrapper.getServiceClass().getClassLoader(),
                new Class<?>[]{serviceWrapper.getServiceClass()}, new DrpcInvocationHandler<>(rpcClientContext, serviceWrapper));
    }
}
