package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.config.RpcClientConfig;
import com.github.xcfyl.drpc.core.config.RpcConfigLoader;
import com.github.xcfyl.drpc.core.protocol.RpcTransferProtocolDecoder;
import com.github.xcfyl.drpc.core.protocol.RpcTransferProtocolEncoder;
import com.github.xcfyl.drpc.core.proxy.jdk.JdkProxyFactory;
import com.github.xcfyl.drpc.core.server.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * rpc客户端
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:29
 */
public class RpcClient {
    private RpcClientConfig config;

    public RpcClient(RpcClientConfig config) {
        this.config = config;
    }

    public RpcReference start() throws Exception {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new RpcTransferProtocolEncoder());
                        channel.pipeline().addLast(new RpcTransferProtocolDecoder());
                        channel.pipeline().addLast(new RpcClientHandler());
                    }
                }).connect(config.getServerIp(), config.getServerPort())
                .sync();
        return new RpcReference(new JdkProxyFactory(channelFuture));
    }

    public static void main(String[] args) throws Throwable {
        RpcClientConfig clientConfig = RpcConfigLoader.loadRpcClientConfig();
        RpcClient rpcClient = new RpcClient(clientConfig);
        RpcReference reference = rpcClient.start();
        HelloService helloService = reference.get(HelloService.class);
        for (int i = 0; i < 100; i++) {
            System.out.println(helloService.hello("zhangsan"));
        }
    }
}
