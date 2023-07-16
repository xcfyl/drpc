package com.github.xcfyl.drpc.core.proxy.jdk;

import com.alibaba.fastjson.JSON;
import com.github.xcfyl.drpc.core.client.DprcConnectionManager;
import com.github.xcfyl.drpc.core.client.DrpcClientContext;
import com.github.xcfyl.drpc.core.client.DrpcConnectionWrapper;
import com.github.xcfyl.drpc.core.client.DrpcServiceWrapper;
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
            TimeUnit.MINUTES, new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());

    private final DrpcServiceWrapper<T> serviceWrapper;
    private final DrpcClientContext rpcClientContext;

    public DrpcInvocationHandler(DrpcClientContext rpcClientContext, DrpcServiceWrapper<T> serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
        this.rpcClientContext = rpcClientContext;
        if (serviceWrapper.getTimeout() == null) {
            serviceWrapper.setTimeout(rpcClientContext.getClientConfig().getRequestTimeout());
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
        while (retryTimes >= 0) {
            threadPoolExecutor.submit(() -> {
                sendRequest(serviceWrapper, requestId, method.getName(), args);
            });
            // 判断是否是同步方法调用，如果是同步方法调用，那么会
            if (serviceWrapper.getSync()) {
                long beginTime = System.currentTimeMillis();
                long timeout = rpcClientContext.getClientConfig().getRequestTimeout();
                while (System.currentTimeMillis() - beginTime < timeout) {
                    DrpcResponse response = rpcClientContext.getResponseCache().get(requestId);
                    if (response != null) {
                        // 将缓存的结果删除
                        rpcClientContext.getResponseCache().remove(requestId);
                        if (response.getThrowable() != null) {
                            // 如果本次请求出现错误，那么重新将该异常进行抛出
                            throw response.getThrowable();
                        }
                        return response.getBody();
                    }
                }
            } else {
                return null;
            }
            retryTimes--;
            if (logger.isDebugEnabled()) {
                logger.debug("retry times {}", serviceWrapper.getRetryTimes() - retryTimes);
            }
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
            DprcConnectionManager connectionManager = rpcClientContext.getConnectionManager();
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
