package com.github.xcfyl.pandarpc.core.client;

import com.github.xcfyl.pandarpc.core.common.config.RpcClientConfig;
import com.github.xcfyl.pandarpc.core.common.config.RpcConfigLoader;
import com.github.xcfyl.pandarpc.core.common.enums.ProxyType;
import com.github.xcfyl.pandarpc.core.common.enums.RegistryDataAttrName;
import com.github.xcfyl.pandarpc.core.common.enums.RegistryType;
import com.github.xcfyl.pandarpc.core.common.utils.CommonUtils;
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
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.github.xcfyl.pandarpc.core.common.enums.ProxyType.JDK;
import static com.github.xcfyl.pandarpc.core.common.enums.RegistryType.ZK;

/**
 * rpc客户端
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:29
 */
@Slf4j
public class RpcClient {
    private final RpcClientConfig config;
    private RpcRegistry registry;

    public RpcClient() {
        config = RpcConfigLoader.loadRpcClientConfig();
    }

    public RpcReference init() {
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
        ProxyType proxyType = config.getProxyType();
        RegistryType registryType = config.getCommonConfig().getRegistryType();
        ProxyFactory proxyFactory;
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

        return new RpcReference(proxyFactory);
    }

    /**
     * 订阅服务
     *
     * @param serviceName
     */
    public void subscribeService(String serviceName) {
        // 在这里订阅服务
        RegistryData registryData = new RegistryData();
        registryData.setApplicationName(config.getCommonConfig().getApplicationName());
        registryData.setIp(CommonUtils.getCurrentMachineIp());
        registryData.setServiceName(serviceName);
        registryData.getAttr().put(RegistryDataAttrName.CREATE_TIME.getDescription(), System.currentTimeMillis());
        registryData.getAttr().put(RegistryDataAttrName.TYPE.getDescription(), "consumer");
        try {
            registry.subscribe(registryData);
            // 订阅之后，尝试和当前服务下的所有服务列表建立连接
            List<RegistryData> providers = registry.queryProviders(serviceName);
            for (RegistryData providerData : providers) {
                String ip = providerData.getIp();
                Integer port = providerData.getPort();
                ConnectionManager.connect(serviceName, ip + ":" + port);
            }
        } catch (Exception e) {
            log.debug("register #{} failure! exception is #{}", serviceName, e.getMessage());
        }
    }

    public static void main(String[] args) throws Throwable {
        RpcClient rpcClient = new RpcClient();
        RpcReference rpcReference = rpcClient.init();
        rpcClient.subscribeService("");
        rpcClient.subscribeService("");
        HelloService helloService = rpcReference.get(HelloService.class);
        String reply = helloService.hello("zhangsan");
        System.out.println(reply);
    }
}
