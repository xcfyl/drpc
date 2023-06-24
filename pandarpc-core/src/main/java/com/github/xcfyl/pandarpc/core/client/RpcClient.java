package com.github.xcfyl.pandarpc.core.client;

import com.github.xcfyl.pandarpc.core.common.config.RpcClientConfig;
import com.github.xcfyl.pandarpc.core.common.config.RpcConfigLoader;
import com.github.xcfyl.pandarpc.core.common.enums.*;
import com.github.xcfyl.pandarpc.core.common.factory.RpcProxyFactory;
import com.github.xcfyl.pandarpc.core.common.factory.RpcRegistryFactory;
import com.github.xcfyl.pandarpc.core.common.factory.RpcRouterFactory;
import com.github.xcfyl.pandarpc.core.common.factory.RpcSerializerFactory;
import com.github.xcfyl.pandarpc.core.common.utils.CommonUtils;
import com.github.xcfyl.pandarpc.core.exception.ConfigErrorException;
import com.github.xcfyl.pandarpc.core.protocol.RpcTransferProtocolDecoder;
import com.github.xcfyl.pandarpc.core.protocol.RpcTransferProtocolEncoder;
import com.github.xcfyl.pandarpc.core.proxy.RpcProxy;
import com.github.xcfyl.pandarpc.core.proxy.jdk.RpcJdkProxy;
import com.github.xcfyl.pandarpc.core.registry.RegistryData;
import com.github.xcfyl.pandarpc.core.registry.RpcRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.github.xcfyl.pandarpc.core.common.enums.RpcProxyType.JDK;

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
        // 创建注册中心
        registry = RpcRegistryFactory.createRpcRegistry(rpcClientConfig.getCommonConfig());
        // 创建路由对象
        RpcClientContext.setRpcRouter(RpcRouterFactory.createRpcRouter(rpcClientConfig));
        // 创建序列化器对象
        RpcClientContext.setSerializer(RpcSerializerFactory.createRpcSerializer(rpcClientConfig.getCommonConfig()));
        // 生成RpcReference对象
        return new RpcReference(RpcProxyFactory.createRpcProxy(rpcClientConfig));
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
