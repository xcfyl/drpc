package com.github.xcfyl.pandarpc.core.server;

import com.alibaba.fastjson.JSON;
import com.github.xcfyl.pandarpc.core.exception.RequestException;
import com.github.xcfyl.pandarpc.core.protocol.RpcRequest;
import com.github.xcfyl.pandarpc.core.protocol.RpcResponse;
import com.github.xcfyl.pandarpc.core.protocol.RpcTransferProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

/**
 * rpc服务端处理器，负责处理请求
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:19
 */
public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcTransferProtocol protocol = (RpcTransferProtocol) msg;
        RpcRequest request = RpcServerContext.getSerializeFactory().deserialize(protocol.getBody(), RpcRequest.class);
        Object service = RpcServerContext.getServiceProviderCache().get(request.getServiceName());
        Method[] methods = service.getClass().getDeclaredMethods();
        Method targetMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(request.getMethodName())) {
                targetMethod = method;
                break;
            }
        }
        if (targetMethod == null) {
            throw new RequestException("未知方法调用");
        }

        Object result = null;
        if (targetMethod.getReturnType() == Void.TYPE) {
            targetMethod.invoke(service, request.getArgs());
        } else {
            result = targetMethod.invoke(service, request.getArgs());
        }
        RpcResponse response = new RpcResponse(request.getId(), result);
        protocol = new RpcTransferProtocol(JSON.toJSONString(response).getBytes());
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
