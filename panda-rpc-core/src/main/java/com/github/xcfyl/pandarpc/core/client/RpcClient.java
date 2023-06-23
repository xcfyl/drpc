package com.github.xcfyl.pandarpc.core.client;

import com.github.xcfyl.pandarpc.core.common.config.RpcClientConfig;
import com.github.xcfyl.pandarpc.core.common.config.RpcConfigLoader;
import com.github.xcfyl.pandarpc.core.common.enums.ProxyType;
import com.github.xcfyl.pandarpc.core.common.enums.RegistryType;
import com.github.xcfyl.pandarpc.core.protocol.RpcTransferProtocolDecoder;
import com.github.xcfyl.pandarpc.core.protocol.RpcTransferProtocolEncoder;
import com.github.xcfyl.pandarpc.core.proxy.ProxyFactory;
import com.github.xcfyl.pandarpc.core.proxy.jdk.JdkProxyFactory;
import com.github.xcfyl.pandarpc.core.registry.RegistryData;
import com.github.xcfyl.pandarpc.core.registry.RpcRegistry;
import com.github.xcfyl.pandarpc.core.registry.zookeeper.ZookeeperClient;
import com.github.xcfyl.pandarpc.core.registry.zookeeper.ZookeeperRegistry;
import com.github.xcfyl.pandarpc.core.server.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import static com.github.xcfyl.pandarpc.core.common.enums.ProxyType.JDK;
import static com.github.xcfyl.pandarpc.core.common.enums.RegistryType.ZK;

/**
 * rpc客户端
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:29
 */
public class RpcClient {
    private final RpcClientConfig config;
    private RpcRegistry registry;
    private ProxyFactory proxyFactory;

    public RpcClient(RpcClientConfig config) {
        this.config = config;
    }

    public RpcReference start() throws Exception {
        Bootstrap bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new RpcTransferProtocolEncoder());
                        channel.pipeline().addLast(new RpcTransferProtocolDecoder());
                        channel.pipeline().addLast(new RpcClientHandler());
                    }
                });
        ConnectionManager.setBootstrap(bootstrap);
        initClient();
        return new RpcReference(new JdkProxyFactory());
    }

    private void initClient() {
        ProxyType proxyType = config.getProxyType();
        RegistryType registryType = config.getCommonConfig().getRegistryType();
        if (proxyType.getCode() == JDK.getCode()) {
            // 说明需要生成jdk动态代理
            proxyFactory = new JdkProxyFactory();
        } else {
            throw new RuntimeException("暂不支持的动态代理类型");
        }

        if (registryType.getCode() == ZK.getCode()) {
            ZookeeperClient zookeeperClient = new ZookeeperClient(config.getCommonConfig().getRegistryAddr());
            registry = new ZookeeperRegistry(zookeeperClient);
        } else {
            throw new RuntimeException("暂不支持的注册中心类型");
        }
    }

    /**
     * 订阅某个服务
     *
     * @param serviceName
     */
    public void subscribeService(String serviceName) {
        // 在这里订阅服务
        RegistryData registryData = new RegistryData();
        registryData.setApplicationName(config.getCommonConfig().getApplicationName());

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
