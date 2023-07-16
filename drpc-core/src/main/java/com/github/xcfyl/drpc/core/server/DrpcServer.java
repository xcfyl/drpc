package com.github.xcfyl.drpc.core.server;

import com.github.xcfyl.drpc.core.common.config.DrpcConfigLoader;
import com.github.xcfyl.drpc.core.common.enums.DrpcAttributeName;
import com.github.xcfyl.drpc.core.common.factory.DrpcRegistryFactory;
import com.github.xcfyl.drpc.core.common.factory.DrpcSerializerFactory;
import com.github.xcfyl.drpc.core.common.utils.DrpcCommonUtils;
import com.github.xcfyl.drpc.core.exception.DrpcClientException;
import com.github.xcfyl.drpc.core.filter.server.DrpcServerFilterChain;
import com.github.xcfyl.drpc.core.filter.server.DrpcServerLogFilter;
import com.github.xcfyl.drpc.core.protocol.DrpcTransferProtocolDecoder;
import com.github.xcfyl.drpc.core.protocol.DrpcTransferProtocolEncoder;
import com.github.xcfyl.drpc.core.registry.DrpcProviderData;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * rpc服务端
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:30
 */
@Slf4j
public class DrpcServer {
    /**
     * Rpc服务器上下文数据
     */
    private final DrpcServerContext context;
    /**
     * 用于执行服务注册的线程池
     */
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8,
            1000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 创建Rpc服务器
     *
     * @param configFileName
     */
    public DrpcServer(String configFileName) {
        context = new DrpcServerContext();
        context.setConfigFileName(configFileName);
    }

    /**
     * 使用默认配置文件启动创建rpc服务器
     */
    public DrpcServer() {
        this("drpc.properties");
    }

    /**
     * 初始化rpc服务端
     *
     * @throws Exception
     */
    public void init() throws Exception {
        DrpcConfigLoader rpcConfigLoader = new DrpcConfigLoader(context.getConfigFileName());
        context.setServerConfig(rpcConfigLoader.loadRpcServerConfig());
        DrpcServerConfig config = context.getServerConfig();
        // 设置注册中心对象
        context.setRegistry(DrpcRegistryFactory.createRpcRegistry(config.getRegistryType(), config.getRegistryAddr()));
        // 创建序列化器对象
        context.setSerializer(DrpcSerializerFactory.createRpcSerializer(config.getSerializeType()));
        // 创建过滤器
        context.setFilterChain(constructServerFilters());
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
                        channel.pipeline().addLast(new DrpcTransferProtocolEncoder());
                        channel.pipeline().addLast(new DrpcTransferProtocolDecoder());
                        channel.pipeline().addLast(new DrpcServerHandler(context));
                    }
                })
                .bind(config.getPort())
                .sync();
    }

    /**
     * 取消服务注册
     *
     * @param service
     */
    public void unregisterService(Object service) {
        // 将当前服务名称写入
        threadPoolExecutor.submit(() -> {
            // 首先将当前服务写入注册中心
            try {
                DrpcProviderData registryData = getProviderRegistryData(service);
                // 将数据从注册中心移除
                context.getRegistry().unregister(registryData);
                // 删除本地缓存的注册数据
                context.getRegistryDataCache().remove(registryData.getServiceName());
                // 删除本地缓存的服务提供者数据
                context.getServiceProviderCache().remove(registryData.getServiceName());
            } catch (Exception e) {
                log.error("register service failure, service name is {}, exception is {}", service, e.getMessage());
            }
        });
    }

    /**
     * 注册服务
     *
     * @param service
     */
    public void registerService(Object service) {
        // 将当前服务名称写入
        threadPoolExecutor.submit(() -> {
            try {
                // 首先将当前服务写入注册中心
                DrpcProviderData registryData = getProviderRegistryData(service);
                context.getRegistry().register(registryData);
                // 将当前服务写入本地缓存中
                context.getRegistryDataCache().put(registryData.getServiceName(), registryData);
                context.getServiceProviderCache().put(registryData.getServiceName(), service);
            } catch (Exception e) {
                log.error("register service failure, service name is {}, exception is {}", service, e.getMessage());
            }
        });
    }

    private DrpcServerFilterChain constructServerFilters() {
        DrpcServerFilterChain filterChain = new DrpcServerFilterChain();
        filterChain.addFilter(new DrpcServerLogFilter());
        return filterChain;
    }

    private DrpcProviderData getProviderRegistryData(Object service) throws Exception {
        // 首先将当前服务写入注册中心
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length != 1) {
            log.error("{} implement too many interface!", service);
            throw new DrpcClientException("implement too many interface");
        }
        Class<?> clazz = interfaces[0];
        String serviceName = clazz.getName();
        DrpcProviderData registryData = new DrpcProviderData();
        registryData.setIp(DrpcCommonUtils.getCurrentMachineIp());
        registryData.setServiceName(serviceName);
        DrpcServerConfig config = context.getServerConfig();
        registryData.setPort(config.getPort());
        registryData.setApplicationName(config.getApplicationName());
        registryData.setAttr(DrpcAttributeName.TYPE.getDescription(), "provider");
        registryData.setAttr(DrpcAttributeName.CREATE_TIME.getDescription(), System.currentTimeMillis());
        return registryData;
    }
}
