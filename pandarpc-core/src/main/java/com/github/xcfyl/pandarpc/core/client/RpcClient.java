package com.github.xcfyl.pandarpc.core.client;

import com.github.xcfyl.pandarpc.core.common.config.RpcClientConfig;
import com.github.xcfyl.pandarpc.core.common.config.RpcConfigLoader;
import com.github.xcfyl.pandarpc.core.common.enums.*;
import com.github.xcfyl.pandarpc.core.common.utils.CommonUtils;
import com.github.xcfyl.pandarpc.core.exception.ConfigErrorException;
import com.github.xcfyl.pandarpc.core.protocol.RpcTransferProtocolDecoder;
import com.github.xcfyl.pandarpc.core.protocol.RpcTransferProtocolEncoder;
import com.github.xcfyl.pandarpc.core.proxy.ProxyFactory;
import com.github.xcfyl.pandarpc.core.proxy.jdk.JdkProxyFactory;
import com.github.xcfyl.pandarpc.core.registry.RegistryData;
import com.github.xcfyl.pandarpc.core.registry.RpcRegistry;
import com.github.xcfyl.pandarpc.core.registry.zookeeper.ZookeeperClient;
import com.github.xcfyl.pandarpc.core.registry.zookeeper.ZookeeperRegistry;
import com.github.xcfyl.pandarpc.core.router.RpcRandomRouter;
import com.github.xcfyl.pandarpc.core.router.RpcRoundRobinRouter;
import com.github.xcfyl.pandarpc.core.serialize.fastjson.FastJsonRpcSerializeFactory;
import com.github.xcfyl.pandarpc.core.serialize.jdk.JdkRpcSerializeFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.github.xcfyl.pandarpc.core.common.enums.RpcProxyType.JDK;
import static com.github.xcfyl.pandarpc.core.common.enums.RpcRegistryType.ZK;

/**
 * rpc客户端
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:29
 */
@Slf4j
public class RpcClient {
    private RpcRegistry registry;

    public RpcClient() {}

    public RpcReference init() throws Exception {
        RpcClientConfig rpcClientConfig = RpcConfigLoader.loadRpcClientConfig();
        RpcClientContext.setRpcClientConfig(rpcClientConfig);

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
        RpcProxyType proxyType = rpcClientConfig.getProxyType();
        ProxyFactory proxyFactory;
        if (proxyType.getCode() == JDK.getCode()) {
            // 说明需要生成jdk动态代理
            proxyFactory = new JdkProxyFactory();
        } else {
            throw new ConfigErrorException("暂不支持的动态代理类型");
        }

        RpcRegistryType registryType = rpcClientConfig.getCommonConfig().getRegistryType();
        if (registryType.getCode() == ZK.getCode()) {
            ZookeeperClient zookeeperClient = new ZookeeperClient(rpcClientConfig.getCommonConfig().getRegistryAddr());
            registry = new ZookeeperRegistry(zookeeperClient);
        } else {
            throw new ConfigErrorException("暂不支持的注册中心类型");
        }

        RpcRouterType routerType = rpcClientConfig.getRouterType();
        if (routerType.getCode() == RpcRouterType.RANDOM.getCode()) {
            RpcClientContext.setRpcRouter(new RpcRandomRouter());
        } else if (routerType.getCode() == RpcRouterType.ROUND_ROBIN.getCode()){
            RpcClientContext.setRpcRouter(new RpcRoundRobinRouter());
        } else {
            throw new ConfigErrorException("暂时不支持的路由类型");
        }

        RpcSerializeType serializeType = rpcClientConfig.getCommonConfig().getSerializeType();
        if (serializeType.getCode() == RpcSerializeType.JDK.getCode()) {
            RpcClientContext.setSerializeFactory(new JdkRpcSerializeFactory());
        } else if (serializeType.getCode() == RpcSerializeType.FASTJSON.getCode()) {
            RpcClientContext.setSerializeFactory(new FastJsonRpcSerializeFactory());
        } else {
            throw new ConfigErrorException("暂时不支持的序列化类型");
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
        RpcClientConfig rpcClientConfig = RpcClientContext.getRpcClientConfig();
        registryData.setApplicationName(rpcClientConfig.getCommonConfig().getApplicationName());
        registryData.setIp(CommonUtils.getCurrentMachineIp());
        registryData.setServiceName(serviceName);
        registryData.getAttr().put(RpcRegistryDataAttrName.CREATE_TIME.getDescription(), System.currentTimeMillis());
        registryData.getAttr().put(RpcRegistryDataAttrName.TYPE.getDescription(), "consumer");
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
}
