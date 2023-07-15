package com.github.xcfyl.drpc.core.server;

import com.alibaba.fastjson.JSON;
import com.github.xcfyl.drpc.core.exception.DrpcRequestException;
import com.github.xcfyl.drpc.core.protocol.DrpcRequest;
import com.github.xcfyl.drpc.core.protocol.DrpcResponse;
import com.github.xcfyl.drpc.core.protocol.DrpcTransferProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

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
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
    private final DrpcServerContext rpcServerContext;

    public DrpcServerHandler(DrpcServerContext rpcServerContext) {
        this.rpcServerContext = rpcServerContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DrpcTransferProtocol protocol = (DrpcTransferProtocol) msg;
        DrpcRequest request = rpcServerContext.getSerializer().deserialize(protocol.getBody(), DrpcRequest.class);
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
            throw new DrpcRequestException("未知方法调用");
        }

        Object result = null;
        if (targetMethod.getReturnType() == Void.TYPE) {
            targetMethod.invoke(service, request.getArgs());
        } else {
            result = targetMethod.invoke(service, request.getArgs());
        }
        DrpcResponse response = new DrpcResponse(request.getId(), result);
        protocol = new DrpcTransferProtocol(JSON.toJSONString(response).getBytes());
        ctx.writeAndFlush(protocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }
}
