package com.github.xcfyl.pandarpc.core.protocol;

import com.github.xcfyl.pandarpc.core.common.RpcContext;
import com.github.xcfyl.pandarpc.core.exception.RequestException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * rpc传输协议解码器，将字节数组解码为RpcTransferProtocol对象
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:50
 */
public class RpcTransferProtocolDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readIndex = byteBuf.readerIndex();
        byteBuf.markReaderIndex();
        if (byteBuf.readShort() != RpcTransferProtocol.getMagicNumber()) {
            ctx.close();
            throw new RequestException("未知rpc协议");
        }

        int length = byteBuf.readInt();
        if (length >= RpcContext.getMaxRequestLength()) {
            ctx.close();
            throw new RequestException("请求长度超过最大限制");
        }

        if (byteBuf.readableBytes() < length) {
            // 当前数据没有完整到来
            byteBuf.readerIndex(readIndex);
            return;
        }

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        RpcTransferProtocol protocol = new RpcTransferProtocol(bytes);
        list.add(protocol);
    }
}
