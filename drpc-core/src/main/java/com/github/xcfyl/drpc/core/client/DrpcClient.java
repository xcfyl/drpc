package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.common.config.DrpcConfigLoader;
import com.github.xcfyl.drpc.core.common.enums.DrpcAttributeName;
import com.github.xcfyl.drpc.core.common.factory.DrpcProxyFactory;
import com.github.xcfyl.drpc.core.common.factory.DrpcRegistryFactory;
import com.github.xcfyl.drpc.core.common.factory.DrpcRouterFactory;
import com.github.xcfyl.drpc.core.common.factory.DrpcSerializerFactory;
import com.github.xcfyl.drpc.core.common.utils.DrpcCommonUtils;
import com.github.xcfyl.drpc.core.filter.client.DrpcClientFilterChain;
import com.github.xcfyl.drpc.core.filter.client.DrpcClientLogFilter;
import com.github.xcfyl.drpc.core.protocol.DrpcTransferProtocolDecoder;
import com.github.xcfyl.drpc.core.protocol.DrpcTransferProtocolEncoder;
import com.github.xcfyl.drpc.core.pubsub.DrpcEventPublisher;
import com.github.xcfyl.drpc.core.pubsub.DrpcServiceChangeEventListener;
import com.github.xcfyl.drpc.core.registry.DrpcConsumerData;
import com.github.xcfyl.drpc.core.registry.DrpcProviderData;
import com.github.xcfyl.drpc.core.registry.DrpcRegistry;
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
public class DrpcClient {
    private final DrpcClientContext context;

    public DrpcClient(String configFileName) {
        context = new DrpcClientContext();
        context.setConfigFileName(configFileName);
        DrpcConfigLoader rpcConfigLoader = new DrpcConfigLoader(configFileName);
        context.setClientConfig(rpcConfigLoader.loadRpcClientConfig());
    }

    public DrpcClient() {
        this("drpc.properties");
    }

    public DrpcRemoteReference init() throws Exception {
        DrpcClientConfig config = context.getClientConfig();
        // 创建注册中心
        context.setRegistry(DrpcRegistryFactory.createRpcRegistry(config.getRegistryType(), config.getRegistryAddr()));
        // 创建过滤器对象
        context.setFilterChain(constructClientFilters());
        // 创建序列化器
        context.setSerializer(DrpcSerializerFactory.createRpcSerializer(config.getSerializeType()));
        // 设置连接处理器
        context.setConnectionManager(new DprcConnectionManager(createBootstrap()));
        // 创建路由对象
        context.setRouter(DrpcRouterFactory.createRpcRouter(config.getRouterType(), context.getConnectionManager()));
        // 注册客户端的事件监听器
        registerClientEventListener();
        // 生成RpcReference对象
        return new DrpcRemoteReference(DrpcProxyFactory.createRpcProxy(config.getProxyType(), context));
    }

    /**
     * 订阅服务
     *
     * @param serviceName
     */
    public void subscribeService(String serviceName) {
        try {
            DrpcConsumerData registryData = getConsumerRegistryData(serviceName);
            DrpcRegistry registry = context.getRegistry();
            registry.subscribe(registryData);
            // 订阅之后，尝试和当前服务下的所有服务列表建立连接
            List<DrpcProviderData> providers = registry.queryProviders(serviceName);
            DprcConnectionManager connectionManager = context.getConnectionManager();
            for (DrpcProviderData providerData : providers) {
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

    private DrpcClientFilterChain constructClientFilters() {
        // 创建过滤器对象
        DrpcClientFilterChain filterChain = new DrpcClientFilterChain();
        filterChain.addFilter(new DrpcClientLogFilter());
        return filterChain;
    }

    private DrpcConsumerData getConsumerRegistryData(String serviceName) {
        // 在这里订阅服务
        DrpcConsumerData registryData = new DrpcConsumerData();
        registryData.setApplicationName(context.getClientConfig().getApplicationName());
        registryData.setIp(DrpcCommonUtils.getCurrentMachineIp());
        registryData.setServiceName(serviceName);
        registryData.setAttr(DrpcAttributeName.CREATE_TIME.getDescription(), System.currentTimeMillis());
        registryData.setAttr(DrpcAttributeName.TYPE.getDescription(), "consumer");
        return registryData;
    }

    private void registerClientEventListener() {
        DrpcEventPublisher eventPublisher = DrpcEventPublisher.getInstance();
        eventPublisher.addEventListener(new DrpcServiceChangeEventListener(context));
    }

    private Bootstrap createBootstrap() {
        return new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new DrpcTransferProtocolEncoder());
                        channel.pipeline().addLast(new DrpcTransferProtocolDecoder());
                        channel.pipeline().addLast(new DrpcClientHandler(context));
                    }
                });
    }
}
