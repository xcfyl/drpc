package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.common.config.RpcClientConfig;
import com.github.xcfyl.drpc.core.common.config.RpcConfigLoader;
import com.github.xcfyl.drpc.core.common.enums.RpcRegistryDataAttrName;
import com.github.xcfyl.drpc.core.common.factory.RpcProxyFactory;
import com.github.xcfyl.drpc.core.common.factory.RpcRegistryFactory;
import com.github.xcfyl.drpc.core.common.factory.RpcRouterFactory;
import com.github.xcfyl.drpc.core.common.factory.RpcSerializerFactory;
import com.github.xcfyl.drpc.core.common.utils.CommonUtils;
import com.github.xcfyl.drpc.core.filter.client.RpcClientFilterChain;
import com.github.xcfyl.drpc.core.filter.client.RpcClientLogFilter;
import com.github.xcfyl.drpc.core.protocol.RpcTransferProtocolDecoder;
import com.github.xcfyl.drpc.core.protocol.RpcTransferProtocolEncoder;
import com.github.xcfyl.drpc.core.registry.RegistryData;
import com.github.xcfyl.drpc.core.registry.RpcRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
        RpcClientContext.setClientConfig(rpcClientConfig);
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
        RpcClientContext.setRouter(RpcRouterFactory.createRpcRouter(rpcClientConfig));
        // 创建序列化器对象
        RpcClientContext.setSerializer(RpcSerializerFactory.createRpcSerializer(rpcClientConfig.getCommonConfig()));
        // 创建过滤器对象
        RpcClientFilterChain filterChain = new RpcClientFilterChain();
        filterChain.addFilter(new RpcClientLogFilter());
        RpcClientContext.setFilterChain(filterChain);
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
        RpcClientConfig rpcClientConfig = RpcClientContext.getClientConfig();
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
