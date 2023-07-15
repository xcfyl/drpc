package com.github.xcfyl.drpc.core.client;

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
import com.github.xcfyl.drpc.core.pubsub.RpcEventPublisher;
import com.github.xcfyl.drpc.core.pubsub.listener.ServiceUpdateEventListener;
import com.github.xcfyl.drpc.core.registry.ConsumerRegistryData;
import com.github.xcfyl.drpc.core.registry.ProviderRegistryData;
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
    private final RpcClientContext context;

    public RpcClient(String configFileName) {
        context = new RpcClientContext();
        context.setConfigFileName(configFileName);
        RpcConfigLoader rpcConfigLoader = new RpcConfigLoader(configFileName);
        context.setClientConfig(rpcConfigLoader.loadRpcClientConfig());
    }

    public RpcClient() {
        this("drpc.properties");
    }

    public RpcReference init() throws Exception {
        RpcClientConfig config = context.getClientConfig();
        // 创建注册中心
        context.setRegistry(RpcRegistryFactory.createRpcRegistry(config.getRegistryType(), config.getRegistryAddr()));
        // 创建过滤器对象
        context.setFilterChain(constructClientFilters());
        // 创建序列化器
        context.setSerializer(RpcSerializerFactory.createRpcSerializer(config.getSerializeType()));
        // 设置连接处理器
        context.setConnectionManager(new ConnectionManager(createBootstrap()));
        // 创建路由对象
        context.setRouter(RpcRouterFactory.createRpcRouter(config.getRouterType(), context.getConnectionManager()));
        // 注册客户端的事件监听器
        registerClientEventListener();
        // 生成RpcReference对象
        return new RpcReference(RpcProxyFactory.createRpcProxy(config.getProxyType(), context));
    }

    /**
     * 订阅服务
     *
     * @param serviceName
     */
    public void subscribeService(String serviceName) {
        try {
            ConsumerRegistryData registryData = getConsumerRegistryData(serviceName);
            RpcRegistry registry = context.getRegistry();
            registry.subscribe(registryData);
            // 订阅之后，尝试和当前服务下的所有服务列表建立连接
            List<ProviderRegistryData> providers = registry.queryProviders(serviceName);
            ConnectionManager connectionManager = context.getConnectionManager();
            for (ProviderRegistryData providerData : providers) {
                String ip = providerData.getIp();
                Integer port = providerData.getPort();
                connectionManager.connect(serviceName, ip + ":" + port);
            }
            // 连接完成之后，应该刷新路由
            context.getRouter().refresh(serviceName);
        } catch (Exception e) {
            log.debug("register #{} failure! exception is #{}", serviceName, e.getMessage());
        }
    }

    private RpcClientFilterChain constructClientFilters() {
        // 创建过滤器对象
        RpcClientFilterChain filterChain = new RpcClientFilterChain();
        filterChain.addFilter(new RpcClientLogFilter());
        return filterChain;
    }

    private ConsumerRegistryData getConsumerRegistryData(String serviceName) {
        // 在这里订阅服务
        ConsumerRegistryData registryData = new ConsumerRegistryData();
        registryData.setApplicationName(context.getClientConfig().getApplicationName());
        registryData.setIp(CommonUtils.getCurrentMachineIp());
        registryData.setServiceName(serviceName);
        registryData.setAttr(RpcRegistryDataAttrName.CREATE_TIME.getDescription(), System.currentTimeMillis());
        registryData.setAttr(RpcRegistryDataAttrName.TYPE.getDescription(), "consumer");
        return registryData;
    }

    private void registerClientEventListener() {
        RpcEventPublisher eventPublisher = RpcEventPublisher.getInstance();
        eventPublisher.addEventListener(new ServiceUpdateEventListener(context));
    }

    private Bootstrap createBootstrap() {
        return new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new RpcTransferProtocolEncoder());
                        channel.pipeline().addLast(new RpcTransferProtocolDecoder());
                        channel.pipeline().addLast(new RpcClientHandler(context));
                    }
                });
    }
}
