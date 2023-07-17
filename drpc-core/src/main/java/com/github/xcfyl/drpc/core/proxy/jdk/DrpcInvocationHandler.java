package com.github.xcfyl.drpc.core.proxy.jdk;

import com.alibaba.fastjson.JSON;
import com.github.xcfyl.drpc.core.client.DrpcClientContext;
import com.github.xcfyl.drpc.core.client.DrpcConnectionManager;
import com.github.xcfyl.drpc.core.client.DrpcConnectionWrapper;
import com.github.xcfyl.drpc.core.client.DrpcServiceWrapper;
import com.github.xcfyl.drpc.core.common.retry.RetryUtils;
import com.github.xcfyl.drpc.core.exception.DrpcRequestException;
import com.github.xcfyl.drpc.core.protocol.DrpcRequest;
import com.github.xcfyl.drpc.core.protocol.DrpcResponse;
import com.github.xcfyl.drpc.core.protocol.DrpcTransferProtocol;
import com.github.xcfyl.drpc.core.router.DrpcRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * rpc客户端发起rpc请求的时候，代理类的执行逻辑
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 11:17
 */
public class DrpcInvocationHandler<T> implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(DrpcInvocationHandler.class);
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 30,
            TimeUnit.MINUTES, new ArrayBlockingQueue<>(3000), new ThreadPoolExecutor.CallerRunsPolicy());

    private final DrpcServiceWrapper<T> serviceWrapper;
    private final DrpcClientContext rpcClientContext;

    public DrpcInvocationHandler(DrpcClientContext rpcClientContext, DrpcServiceWrapper<T> serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
        this.rpcClientContext = rpcClientContext;
        if (serviceWrapper.getTimeout() == null) {
            serviceWrapper.setTimeout(rpcClientContext.getClientConfig().getRequestTimeout());
        }
        if (serviceWrapper.getRetryTimes() == null) {
            serviceWrapper.setRetryTimes(rpcClientContext.getClientConfig().getRequestRetryTimes());
        }
        if (serviceWrapper.getRetryInterval() == null) {
            serviceWrapper.setRetryInterval(rpcClientContext.getClientConfig().getRequestRetryInterval());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("service wrapper is {}", serviceWrapper);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 生成本次请求id
        String requestId = UUID.randomUUID().toString();
        Integer retryTimes = serviceWrapper.getRetryTimes();
        Long retryInterval = serviceWrapper.getRetryInterval();

        // 异步写数据
        threadPoolExecutor.submit(() -> sendRequest(serviceWrapper, requestId, method.getName(), args));
        if (!serviceWrapper.isSync()) {
            // 如果不是同步请求，直接返回
            return null;
        }
        // 如果是同步请求，那么通过guard获取数据
        DrpcResponse response = rpcClientContext.getResponseGuardedObject()
                .getDrpcResponse(requestId, serviceWrapper.getTimeout());

        if (response == null) {
            // 如果第一次请求超时
            response = RetryUtils.retry("GetResponseRetry", retryTimes, retryInterval, () -> {
                // 在这里面编写重试的逻辑
                return rpcClientContext.getResponseGuardedObject()
                        .getDrpcResponse(requestId, serviceWrapper.getTimeout());
            }, Objects::nonNull);
        }

        // 如果重试成功，那么尝试获取本次请求的结果
        if (response != null) {
            if (response.getThrowable() != null) {
                throw response.getThrowable();
            }
            return response.getBody();

        }
        if (logger.isDebugEnabled()) {
            logger.debug("request timeout, request id is {}, timeout limit is {}, retrytimes are {}",
                    requestId, serviceWrapper.getTimeout(), retryTimes);
        }
        throw new DrpcRequestException("request timeout");
    }

    private void sendRequest(DrpcServiceWrapper<?> serviceWrapper, String requestId, String methodName, Object[] args) {
        try {
            // 当前调用的服务的名称
            String serviceName = serviceWrapper.getServiceClass().getName();
            // 当前调用的服务的方法名称
            // 创建Rpc请求对象
            DrpcRequest request = new DrpcRequest(requestId, serviceName, methodName, args);
            // 创建rpc协议对象
            DrpcTransferProtocol protocol = new DrpcTransferProtocol(JSON.toJSONString(request).getBytes());
            // 获取客户端连接管理器对象
            DrpcConnectionManager connectionManager = rpcClientContext.getConnectionManager();
            // 获取当前客户端本地缓存的所有连接对象
            List<DrpcConnectionWrapper> originalConnections = connectionManager.getOriginalConnections(serviceName);
            // 复制一份原始连接对象，交给过滤器进行过滤
            List<DrpcConnectionWrapper> filteredConnections = new ArrayList<>(originalConnections);
            // 调用过滤器对连接对象进行过滤
            rpcClientContext.getFilterChain().doFilter(filteredConnections, request);
            // 获取当前客户端的路由对象
            DrpcRouter router = rpcClientContext.getRouter();
            // 使用路由对象从过滤后的连接对象中选择一个连接
            DrpcConnectionWrapper connectionWrapper = router.select(serviceName);
            // 使用连接对象将该rpc协议对象发送给服务提供者
            connectionWrapper.writeAndFlush(protocol);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("send request exception -> {}", e.getMessage());
        }
    }
}
