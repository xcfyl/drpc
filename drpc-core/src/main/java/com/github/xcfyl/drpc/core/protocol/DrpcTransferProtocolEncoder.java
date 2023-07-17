package com.github.xcfyl.drpc.core.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rpc传输协议编码器，将RpcTransferProtocol编码为字节数组对象
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:51
 */
public class DrpcTransferProtocolEncoder extends MessageToByteEncoder<DrpcTransferProtocol> {
    private static final Logger logger = LoggerFactory.getLogger(DrpcTransferProtocolEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, DrpcTransferProtocol protocol, ByteBuf byteBuf) throws Exception {
        byteBuf.writeShort(DrpcTransferProtocol.getMagicNumber());
        byteBuf.writeInt(protocol.getLength());
        byteBuf.writeBytes(protocol.getBody());
    }
}
