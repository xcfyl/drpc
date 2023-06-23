package com.github.xcfyl.pandarpc.core.client;

import com.github.xcfyl.pandarpc.core.protocol.RpcResponse;
import com.github.xcfyl.pandarpc.core.protocol.RpcTransferProtocol;
import com.github.xcfyl.pandarpc.core.utils.RpcTransferProtocolHelper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import static com.github.xcfyl.pandarpc.core.client.RpcClientLocalCache.RESPONSE_MAP;

/**
 * rpc客户端处理器
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:30
 */
public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcTransferProtocol protocol = (RpcTransferProtocol) msg;
        RpcResponse response = RpcTransferProtocolHelper.parseRpcResponse(protocol);
        RESPONSE_MAP.put(response.getId(), response);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        if(channel.isActive()){
            ctx.close();
        }
    }
}
