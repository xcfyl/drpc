package com.github.xcfyl.drpc.core.server;

import com.github.xcfyl.drpc.core.config.RpcConfigLoader;
import com.github.xcfyl.drpc.core.protocol.RpcTransferProtocolDecoder;
import com.github.xcfyl.drpc.core.protocol.RpcTransferProtocolEncoder;
import com.github.xcfyl.drpc.core.config.RpcServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import static com.github.xcfyl.drpc.core.server.RpcServerLocalCache.SERVICE_PROVIDER_CACHE;

/**
 * rpc服务端
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:30
 */
public class RpcServer {
    private final RpcServerConfig config;

    public RpcServer(RpcServerConfig config) {
        this.config = config;
    }

    /**
     * 启动rpc服务端
     *
     * @throws Exception
     */
    public void start() throws Exception {
        new ServerBootstrap()
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new RpcTransferProtocolEncoder());
                        channel.pipeline().addLast(new RpcTransferProtocolDecoder());
                        channel.pipeline().addLast(new RpcServerHandler());
                    }
                })
                .bind(config.getPort())
                .sync();
    }

    /**
     * 注册服务
     *
     * @param service
     */
    public void registerService(Object service) {
        if (service.getClass().getInterfaces().length == 0) {
            throw new RuntimeException("服务没有实现任何接口");
        }
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length > 1) {
            throw new RuntimeException("服务只能实现一个接口");
        }
        Class<?> interfaceClass = interfaces[0];
        SERVICE_PROVIDER_CACHE.put(interfaceClass.getName(), service);
    }

    public static void main(String[] args) throws Throwable {
        RpcServerConfig serverConfig = RpcConfigLoader.loadRpcServerConfig();
        RpcServer rpcServer = new RpcServer(serverConfig);
        rpcServer.registerService(new HelloServiceImpl());
        rpcServer.start();
    }
}
