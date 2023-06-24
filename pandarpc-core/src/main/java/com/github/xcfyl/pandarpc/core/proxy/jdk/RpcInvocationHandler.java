package com.github.xcfyl.pandarpc.core.proxy.jdk;

import com.alibaba.fastjson.JSON;
import com.github.xcfyl.pandarpc.core.client.ConnectionWrapper;
import com.github.xcfyl.pandarpc.core.client.RpcRouterRef;
import com.github.xcfyl.pandarpc.core.client.SubscribedServiceWrapper;
import com.github.xcfyl.pandarpc.core.common.RpcContext;
import com.github.xcfyl.pandarpc.core.client.RpcClientLocalCache;
import com.github.xcfyl.pandarpc.core.protocol.RpcRequest;
import com.github.xcfyl.pandarpc.core.protocol.RpcResponse;
import com.github.xcfyl.pandarpc.core.protocol.RpcTransferProtocol;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * rpc客户端发起rpc请求的时候，代理类的执行逻辑
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 11:17
 */
public class RpcInvocationHandler<T> implements InvocationHandler {
    private final SubscribedServiceWrapper<T> serviceWrapper;

    public RpcInvocationHandler(SubscribedServiceWrapper<T> serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String requestId = UUID.randomUUID().toString();
        String serviceName = serviceWrapper.getServiceClass().getName();
        String methodName = method.getName();
        RpcRequest request = new RpcRequest(requestId, serviceName, methodName, args);
        RpcTransferProtocol protocol = new RpcTransferProtocol(JSON.toJSONString(request).getBytes());
        ConnectionWrapper connectionWrapper = RpcRouterRef.getRpcRouter().select(serviceName);
        connectionWrapper.writeAndFlush(protocol);

        // 判断是否是同步方法调用
        if (serviceWrapper.isSync()) {
            long beginTime = System.currentTimeMillis();
            long timeout = RpcContext.getRequestTimeout();
            while (System.currentTimeMillis() - beginTime < timeout) {
                RpcResponse response = RpcClientLocalCache.RESPONSE_MAP.get(requestId);
                if (response != null) {
                    return response.getBody();
                }
            }
            throw new TimeoutException("请求超时");
        }
        return null;
    }
}
