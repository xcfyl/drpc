package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.common.config.ConfigLoader;
import com.github.xcfyl.drpc.core.common.enums.AttributeName;
import com.github.xcfyl.drpc.core.common.factory.ProxyFactory;
import com.github.xcfyl.drpc.core.common.factory.RegistryFactory;
import com.github.xcfyl.drpc.core.common.factory.RouterFactory;
import com.github.xcfyl.drpc.core.common.factory.SerializerFactory;
import com.github.xcfyl.drpc.core.common.utils.CommonUtils;
import com.github.xcfyl.drpc.core.filter.client.ClientFilterChain;
import com.github.xcfyl.drpc.core.filter.client.ClientLogFilter;
import com.github.xcfyl.drpc.core.protocol.RpcTransferProtocolDecoder;
import com.github.xcfyl.drpc.core.protocol.RpcTransferProtocolEncoder;
import com.github.xcfyl.drpc.core.pubsub.RpcEventPublisher;
import com.github.xcfyl.drpc.core.pubsub.listener.ServiceUpdateEventListener;
import com.github.xcfyl.drpc.core.registry.ConsumerData;
import com.github.xcfyl.drpc.core.registry.ProviderData;
import com.github.xcfyl.drpc.core.registry.Registry;
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
    private final ClientContext context;

    public RpcClient(String configFileName) {
        context = new ClientContext();
        context.setConfigFileName(configFileName);
        ConfigLoader rpcConfigLoader = new ConfigLoader(configFileName);
        context.setClientConfig(rpcConfigLoader.loadRpcClientConfig());
    }

    public RpcClient() {
        this("drpc.properties");
    }

    public RemoteReference init() throws Exception {
        ClientConfig config = context.getClientConfig();
        // 创建注册中心
        context.setRegistry(RegistryFactory.createRpcRegistry(config.getRegistryType(), config.getRegistryAddr()));
        // 创建过滤器对象
        context.setFilterChain(constructClientFilters());
        // 创建序列化器
        context.setSerializer(SerializerFactory.createRpcSerializer(config.getSerializeType()));
        // 设置连接处理器
        context.setConnectionManager(new ConnectionManager(createBootstrap()));
        // 创建路由对象
        context.setRouter(RouterFactory.createRpcRouter(config.getRouterType(), context.getConnectionManager()));
        // 注册客户端的事件监听器
        registerClientEventListener();
        // 生成RpcReference对象
        return new RemoteReference(ProxyFactory.createRpcProxy(config.getProxyType(), context));
    }

    /**
     * 订阅服务
     *
     * @param serviceName
     */
    public void subscribeService(String serviceName) {
        try {
            ConsumerData registryData = getConsumerRegistryData(serviceName);
            Registry registry = context.getRegistry();
            registry.subscribe(registryData);
            // 订阅之后，尝试和当前服务下的所有服务列表建立连接
            List<ProviderData> providers = registry.queryProviders(serviceName);
            ConnectionManager connectionManager = context.getConnectionManager();
            for (ProviderData providerData : providers) {
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

    private ClientFilterChain constructClientFilters() {
        // 创建过滤器对象
        ClientFilterChain filterChain = new ClientFilterChain();
        filterChain.addFilter(new ClientLogFilter());
        return filterChain;
    }

    private ConsumerData getConsumerRegistryData(String serviceName) {
        // 在这里订阅服务
        ConsumerData registryData = new ConsumerData();
        registryData.setApplicationName(context.getClientConfig().getApplicationName());
        registryData.setIp(CommonUtils.getCurrentMachineIp());
        registryData.setServiceName(serviceName);
        registryData.setAttr(AttributeName.CREATE_TIME.getDescription(), System.currentTimeMillis());
        registryData.setAttr(AttributeName.TYPE.getDescription(), "consumer");
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
                        channel.pipeline().addLast(new ClientHandler(context));
                    }
                });
    }
}
