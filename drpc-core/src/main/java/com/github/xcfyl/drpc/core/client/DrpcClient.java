package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.common.config.DrpcConfigLoader;
import com.github.xcfyl.drpc.core.common.enums.DrpcAttributeName;
import com.github.xcfyl.drpc.core.common.factory.DrpcProxyFactory;
import com.github.xcfyl.drpc.core.common.factory.DrpcRegistryFactory;
import com.github.xcfyl.drpc.core.common.factory.DrpcRouterFactory;
import com.github.xcfyl.drpc.core.common.factory.DrpcSerializerFactory;
import com.github.xcfyl.drpc.core.common.retry.RetryUtils;
import com.github.xcfyl.drpc.core.common.utils.DrpcCommonUtils;
import com.github.xcfyl.drpc.core.exception.DrpcClientException;
import com.github.xcfyl.drpc.core.filter.client.DrpcClientFilter;
import com.github.xcfyl.drpc.core.filter.client.DrpcClientFilterChain;
import com.github.xcfyl.drpc.core.filter.client.DrpcClientLogFilter;
import com.github.xcfyl.drpc.core.protocol.DrpcTransferProtocolDecoder;
import com.github.xcfyl.drpc.core.protocol.DrpcTransferProtocolEncoder;
import com.github.xcfyl.drpc.core.pubsub.DrpcEventPublisher;
import com.github.xcfyl.drpc.core.pubsub.listener.DrpcServiceChangeEventListener;
import com.github.xcfyl.drpc.core.registry.DrpcConsumerData;
import com.github.xcfyl.drpc.core.registry.DrpcProviderData;
import com.github.xcfyl.drpc.core.registry.DrpcRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * rpc客户端
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:29
 */
public class DrpcClient {
    private static final Logger logger = LoggerFactory.getLogger(DrpcClient.class);
    private final DrpcClientContext context;

    public DrpcClient(String configFileName) {
        context = new DrpcClientContext();
        context.setConfigFileName(configFileName);
    }

    public DrpcClient() {
        this("drpc.properties");
    }

    public void init() throws Exception {
        DrpcConfigLoader rpcConfigLoader = new DrpcConfigLoader(context.getConfigFileName());
        context.setClientConfig(rpcConfigLoader.loadRpcClientConfig());
        DrpcClientConfig config = context.getClientConfig();
        // 创建注册中心
        context.setRegistry(DrpcRegistryFactory.createRpcRegistry(config.getRegistryType(), config.getRegistryAddr()));
        // 创建过滤器对象
        context.setFilterChain(constructClientFilters());
        // 创建序列化器
        context.setSerializer(DrpcSerializerFactory.createRpcSerializer(config.getSerializeType()));
        // 设置连接处理器
        DrpcConnectionManager connectionManager = new DrpcConnectionManager(createBootstrap());
        connectionManager.setRetryConnectTimes(config.getReconnectTimes());
        connectionManager.setRetryConnectInterval(connectionManager.getRetryConnectInterval());
        context.setConnectionManager(connectionManager);
        // 创建路由对象
        context.setRouter(DrpcRouterFactory.createRpcRouter(config.getRouterType(), context.getConnectionManager()));
        // 创建同步请求对象
        context.setResponseGuardedObject(new DrpcResponseGuardedObject());
        // 注册客户端的事件监听器
        registerClientEventListener();
    }

    public DrpcRemoteReference getDrpcRemoteReference() {
        // 生成RpcReference对象
        DrpcClientConfig config = context.getClientConfig();
        return new DrpcRemoteReference(DrpcProxyFactory.createRpcProxy(config.getProxyType(), context));
    }

    /**
     * 订阅服务
     *
     * @param serviceName
     */
    public void subscribeService(String serviceName) throws Exception {
        DrpcRegistry registry = context.getRegistry();
        Integer retryTimes = context.getClientConfig().getSubscribeRetryTimes();
        Long retryInterval = context.getClientConfig().getSubscribeRetryInterval();
        List<DrpcProviderData> providers = registry.queryProviders(serviceName);

        if (providers == null || providers.size() == 0) {
            // 如果没有获取到providers信息，那么尝试重复获取
            providers = RetryUtils.retry("queryProvidersRetry", retryTimes, retryInterval,
                    () -> registry.queryProviders(serviceName),
                    providers1 -> providers1 != null && providers1.size() != 0);
        }
        // 如果经过尝试之后，还是没有获取到，那么说明确实没有providers发现
        if (providers == null || providers.isEmpty()) {
            logger.error("subscribe service {} failure, no providers found", serviceName);
            throw new DrpcClientException("subscribe service failure, no providers found!");
        }
        DrpcConsumerData registryData = getConsumerRegistryData(serviceName);
        registry.subscribe(registryData);
        DrpcConnectionManager connectionManager = context.getConnectionManager();
        for (DrpcProviderData providerData : providers) {
            String ip = providerData.getIp();
            Integer port = providerData.getPort();
            connectionManager.connect(serviceName, ip, port);
        }
        // 连接完成之后，应该刷新路由
        context.getRouter().refresh(serviceName);
    }

    public void addFilter(DrpcClientFilter filter) {
        context.getFilterChain().addFilter(filter);
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
        // 注册用于监听服务变更的事件监听器
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
