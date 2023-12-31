package com.github.xcfyl.drpc.core.protocol;

import com.github.xcfyl.drpc.core.exception.DrpcRequestException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * rpc传输协议解码器，将字节数组解码为RpcTransferProtocol对象
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:50
 */
public class DrpcTransferProtocolDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(DrpcTransferProtocolDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() >= DrpcTransferProtocol.getHeaderLength()) {
            int readIndex = byteBuf.readerIndex();
            byteBuf.markReaderIndex();
            if (byteBuf.readShort() != DrpcTransferProtocol.getMagicNumber()) {
                ctx.close();
                throw new DrpcRequestException("未知rpc协议");
            }

            int length = byteBuf.readInt();
            if (byteBuf.readableBytes() < length) {
                // 当前数据没有完整到来
                byteBuf.readerIndex(readIndex);
                return;
            }

            byte[] bytes = new byte[length];
            byteBuf.readBytes(bytes);
            DrpcTransferProtocol protocol = new DrpcTransferProtocol(bytes);
            list.add(protocol);
        }
    }
}
