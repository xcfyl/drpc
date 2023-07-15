package com.github.xcfyl.drpc.core.proxy.jdk;

import com.alibaba.fastjson.JSON;
import com.github.xcfyl.drpc.core.client.ConnectionManager;
import com.github.xcfyl.drpc.core.client.ConnectionWrapper;
import com.github.xcfyl.drpc.core.client.ClientContext;
import com.github.xcfyl.drpc.core.client.ServiceWrapper;
import com.github.xcfyl.drpc.core.protocol.RpcRequest;
import com.github.xcfyl.drpc.core.protocol.RpcResponse;
import com.github.xcfyl.drpc.core.protocol.RpcTransferProtocol;
import com.github.xcfyl.drpc.core.router.Router;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * rpc客户端发起rpc请求的时候，代理类的执行逻辑
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 11:17
 */
@Slf4j
public class RpcInvocationHandler<T> implements InvocationHandler {
    private final ServiceWrapper<T> serviceWrapper;
    private final ClientContext rpcClientContext;

    public RpcInvocationHandler(ClientContext rpcClientContext, ServiceWrapper<T> serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
        this.rpcClientContext = rpcClientContext;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 生成本次请求id
        String requestId = UUID.randomUUID().toString();
        // 当前调用的服务的名称
        String serviceName = serviceWrapper.getServiceClass().getName();
        // 当前调用的服务的方法名称
        String methodName = method.getName();
        // 创建Rpc请求对象
        RpcRequest request = new RpcRequest(requestId, serviceName, methodName, args);
        // 创建rpc协议对象
        RpcTransferProtocol protocol = new RpcTransferProtocol(JSON.toJSONString(request).getBytes());
        // 获取客户端连接管理器对象
        ConnectionManager connectionManager = rpcClientContext.getConnectionManager();
        // 获取当前客户端本地缓存的所有连接对象
        List<ConnectionWrapper> originalConnections = connectionManager.getOriginalConnections(serviceName);
        // 复制一份原始连接对象，交给过滤器进行过滤
        List<ConnectionWrapper> filteredConnections = new ArrayList<>(originalConnections);
        // 调用过滤器对连接对象进行过滤
        rpcClientContext.getFilterChain().doFilter(filteredConnections, request);
        // 获取当前客户端的路由对象
        Router router = rpcClientContext.getRouter();
        // 使用路由对象从过滤后的连接对象中选择一个连接
        ConnectionWrapper connectionWrapper = router.select(serviceName);
        // 使用连接对象将该rpc协议对象发送给服务提供者
        connectionWrapper.writeAndFlush(protocol);
        // 判断是否是同步方法调用，如果是同步方法调用，那么会
        if (serviceWrapper.isSync()) {
            long beginTime = System.currentTimeMillis();
            long timeout = rpcClientContext.getClientConfig().getRequestTimeout();
            while (System.currentTimeMillis() - beginTime < timeout) {
                RpcResponse response = rpcClientContext.getResponseCache().get(requestId);
                if (response != null) {
                    return response.getBody();
                }
            }
            throw new TimeoutException("请求超时");
        }
        return null;
    }
}
