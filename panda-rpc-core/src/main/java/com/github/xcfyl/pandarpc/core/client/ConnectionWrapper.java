package com.github.xcfyl.pandarpc.core.client;

import io.netty.channel.ChannelFuture;
import lombok.Data;

/**
 * 封装了连接对象
 *
 * @author 西城风雨楼
 * @date create at 2023/6/23 15:27
 */
@Data
public class ConnectionWrapper {
    /**
     * 代表连接的channelFuture对象
     */
    private ChannelFuture channelFuture;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 端口号
     */
    private Integer port;
}
