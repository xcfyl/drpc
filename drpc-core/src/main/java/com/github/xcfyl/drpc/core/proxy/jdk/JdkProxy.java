package com.github.xcfyl.drpc.core.proxy.jdk;

import com.github.xcfyl.drpc.core.client.ClientContext;
import com.github.xcfyl.drpc.core.client.ServiceWrapper;
import com.github.xcfyl.drpc.core.proxy.RpcProxy;

import java.lang.reflect.Proxy;

/**
 * 基于jdk动态代理模式的代理工厂
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 11:16
 */
public class JdkProxy implements RpcProxy {
    private final ClientContext rpcClientContext;

    public JdkProxy(ClientContext rpcClientContext) {
        this.rpcClientContext = rpcClientContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(ServiceWrapper<T> serviceWrapper) throws Exception {
        return (T) Proxy.newProxyInstance(serviceWrapper.getServiceClass().getClassLoader(),
                new Class<?>[]{serviceWrapper.getServiceClass()}, new RpcInvocationHandler<>(rpcClientContext, serviceWrapper));
    }
}
