package com.github.xcfyl.drpc.core.server;

import com.alibaba.fastjson.JSON;
import com.github.xcfyl.drpc.core.exception.DrpcRequestException;
import com.github.xcfyl.drpc.core.protocol.DrpcRequest;
import com.github.xcfyl.drpc.core.protocol.DrpcResponse;
import com.github.xcfyl.drpc.core.protocol.DrpcTransferProtocol;
import com.github.xcfyl.drpc.core.serializer.DrpcSerializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * rpc服务端处理器，负责处理请求
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:19
 */
public class DrpcServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(DrpcServerHandler.class);
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(8,
            16,
            30,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(3000),
            new ThreadPoolExecutor.CallerRunsPolicy());
    private final DrpcServerContext rpcServerContext;

    public DrpcServerHandler(DrpcServerContext rpcServerContext) {
        this.rpcServerContext = rpcServerContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        executor.submit(() -> {
            DrpcSerializer serializer = rpcServerContext.getSerializer();
            DrpcTransferProtocol protocol = (DrpcTransferProtocol) msg;
            DrpcRequest request;
            try {
                request = serializer.deserialize(protocol.getBody(), DrpcRequest.class);
            } catch (Exception e) {
                logger.error("deserialize request failure, exception is {}", e.getMessage());
                return;
            }
            try {
                // 执行过滤逻辑
                rpcServerContext.getFilterChain().doFilter(request);
                Object service = rpcServerContext.getServiceProviderCache().get(request.getServiceName());
                Method[] methods = service.getClass().getDeclaredMethods();
                Method targetMethod = null;
                for (Method method : methods) {
                    if (method.getName().equals(request.getMethodName())) {
                        targetMethod = method;
                        break;
                    }
                }
                if (targetMethod == null) {
                    throw new DrpcRequestException("unknown method");
                }

                Object result = null;
                try {
                    if (targetMethod.getReturnType() == Void.TYPE) {
                        targetMethod.invoke(service, request.getArgs());
                    } else {
                        result = targetMethod.invoke(service, request.getArgs());
                    }
                } catch (Exception e) {
                    throw new DrpcRequestException("method invoke failure", e);
                }
                DrpcResponse response = new DrpcResponse(request.getId(), result);
                protocol = new DrpcTransferProtocol(JSON.toJSONString(response).getBytes());
                ctx.writeAndFlush(protocol);
            } catch (DrpcRequestException requestException) {
                writeFailure(ctx, serializer, request.getId(), requestException);
            } catch (Exception e) {
                logger.error("rpc server, handle request failure, exception is {}", e.getMessage());
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }

    private void writeFailure(ChannelHandlerContext ctx, DrpcSerializer serializer, String requestId, Throwable throwable) {
        try {
            // 如果方法出错了
            DrpcResponse response = new DrpcResponse();
            response.setId(requestId);
            response.setThrowable(throwable);
            DrpcTransferProtocol transferProtocol = new DrpcTransferProtocol(serializer.serialize(response));
            ctx.writeAndFlush(transferProtocol);
            logger.error("handle request failure -> {}", throwable.getMessage());
        } catch (Exception e) {
            logger.error("writeFailure error -> {}", e.getMessage());
        }
    }
}
