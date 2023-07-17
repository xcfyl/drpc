package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.protocol.DrpcResponse;
import com.github.xcfyl.drpc.core.protocol.DrpcTransferProtocol;
import com.github.xcfyl.drpc.core.serializer.DrpcSerializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * rpc客户端处理器
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:30
 */
public class DrpcClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(DrpcClientHandler.class);
    private final DrpcClientContext rpcClientContext;
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 30,
            TimeUnit.MINUTES, new ArrayBlockingQueue<>(3000), new ThreadPoolExecutor.CallerRunsPolicy());

    public DrpcClientHandler(DrpcClientContext rpcClientContext) {
        this.rpcClientContext = rpcClientContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        threadPoolExecutor.submit(() -> {
            try {
                DrpcTransferProtocol protocol = (DrpcTransferProtocol) msg;
                DrpcSerializer serializer = rpcClientContext.getSerializer();
                DrpcResponse response = serializer.deserialize(protocol.getBody(), DrpcResponse.class);
                 rpcClientContext.getResponseGuardedObject().setDrpcResponse(response);
                if (logger.isDebugEnabled()) {
                    logger.debug("receive a response, {}", response);
                }
                ReferenceCountUtil.release(msg);
            } catch (Exception e) {
                logger.error("client handle response failure {}", e.getMessage());
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        if(channel.isActive()){
            ctx.close();
        }
    }
}
