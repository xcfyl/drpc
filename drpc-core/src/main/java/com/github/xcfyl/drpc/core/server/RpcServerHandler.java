package com.github.xcfyl.drpc.core.server;

import com.alibaba.fastjson.JSON;
import com.github.xcfyl.drpc.core.protocol.RpcRequest;
import com.github.xcfyl.drpc.core.protocol.RpcResponse;
import com.github.xcfyl.drpc.core.protocol.RpcTransferProtocol;
import com.github.xcfyl.drpc.core.utils.RpcTransferProtocolHelper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

import static com.github.xcfyl.drpc.core.server.RpcServerLocalCache.SERVICE_PROVIDER_MAP;

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
        RpcRequest request = RpcTransferProtocolHelper.parseRpcRequest(protocol);
        Object service = SERVICE_PROVIDER_MAP.get(request.getServiceName());
        Method[] methods = service.getClass().getDeclaredMethods();
        Object result = null;
        for (Method method : methods) {
            if (method.getName().equals(request.getMethodName())) {
                if (method.getReturnType().equals(Void.TYPE)) {
                    // 如果方法没有返回值
                    method.invoke(service, request.getArgs());
                } else {
                    result = method.invoke(service, request.getArgs());
                }
                break;
            }
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
