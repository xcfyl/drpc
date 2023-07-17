package com.github.xcfyl.drpc.core.client;


import com.github.xcfyl.drpc.core.common.retry.RetryUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 连接管理器，用于管理当前客户端和远程所有rpc服务的连接
 * 开启定时任务，定期检测连接的活性，如果某个连接活性不行了，尝试进行重连
 * 在达到一定的重连次数的时候，移除该连接
 *
 * @author 西城风雨楼
 * @date create at 2023/6/23 15:26
 */
public class DrpcConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(DrpcConnectionManager.class);
    /**
     * 和客户端的启动器
     */
    private final Bootstrap bootstrap;
    /**
     * 如果连接端开了重试策略
     */
    private Integer retryConnectTimes;
    /**
     * 每次重试的间隔
     */
    private Long retryConnectInterval;
    /**
     * 没有经过过滤器处理的连接缓存
     */
    private final Map<String, CopyOnWriteArrayList<DrpcConnectionWrapper>> originalConnectionCache = new HashMap<>();
    private final CopyOnWriteArrayList<String> serviceNames = new CopyOnWriteArrayList<>();
    /**
     * 保护连接缓存更新的安全性
     */
    private final Lock lock = new ReentrantLock();

    private final ScheduledExecutorService timerTask = new ScheduledThreadPoolExecutor(
            1, new ThreadPoolExecutor.CallerRunsPolicy());

    public DrpcConnectionManager(Bootstrap bootstrap, Integer retryConnectTimes, Long retryConnectInterval) {
        this(bootstrap);
        this.retryConnectInterval = retryConnectInterval;
        this.retryConnectTimes = retryConnectTimes;
    }

    public DrpcConnectionManager(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
        startCheckConnectionAlive();
    }

    private void startCheckConnectionAlive() {
        timerTask.scheduleWithFixedDelay(() -> {
            if (logger.isDebugEnabled()) {
                logger.debug("start check connect alive");
            }
            try {
                for (String serviceName : serviceNames) {
                    List<DrpcConnectionWrapper> originalConnections = getOriginalConnections(serviceName);
                    if (logger.isDebugEnabled()) {
                        logger.debug("check service {}, connections {}", serviceName, originalConnections);
                    }
                    for (DrpcConnectionWrapper connectionWrapper : originalConnections) {
                        if (connectionWrapper.isOk()) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("connection {} is still alive", connectionWrapper);
                            }
                            continue;
                        }

                        if (logger.isDebugEnabled()) {
                            logger.debug("connection {} not alive, retry reconnect it", connectionWrapper);
                        }
                        // 执行重试逻辑
                        RetryUtils.retry(retryConnectTimes, retryConnectInterval, () -> {
                            DrpcConnectionWrapper newConnectionWrapper = getConnectionWrapper(
                                    serviceName, connectionWrapper.getIp(), connectionWrapper.getPort());
                            if (newConnectionWrapper.isOk()) {
                                replace(serviceName, connectionWrapper, newConnectionWrapper);
                            }
                            return newConnectionWrapper.isOk();
                        }, bool -> bool);
                    }
                }
            } catch (Exception e) {
                logger.error("connect check error {}", e.getMessage());
            }
        }, 5, 10, TimeUnit.MINUTES);
    }

    /**
     * 连接某个服务的某个具体的服务提供者
     *
     * @param serviceName
     * @param ip
     * @param port
     */
    public void connect(String serviceName, String ip, Integer port) {
        DrpcConnectionWrapper connectionWrapper = getConnectionWrapper(serviceName, ip, port);
        // 缓存当前连接对象
        try {
            serviceNames.add(serviceName);
            lock.lock();
            CopyOnWriteArrayList<DrpcConnectionWrapper> connectionWrappers =
                    originalConnectionCache.getOrDefault(serviceName, new CopyOnWriteArrayList<>());
            connectionWrappers.add(connectionWrapper);
            originalConnectionCache.put(serviceName, connectionWrappers);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 从连接缓存中移除指定的连接
     *
     * @param serviceName
     * @param connectionWrapper
     */
    public void remove(String serviceName, DrpcConnectionWrapper connectionWrapper) {
         try {
             lock.lock();
             CopyOnWriteArrayList<DrpcConnectionWrapper> connectionWrappers = originalConnectionCache.getOrDefault(serviceName, new CopyOnWriteArrayList<>());
             if (!connectionWrappers.contains(connectionWrapper)) {
                 return;
             }
             connectionWrappers.remove(connectionWrapper);
             if (connectionWrappers.size() == 0) {
                 serviceNames.remove(serviceName);
             }
             originalConnectionCache.put(serviceName, connectionWrappers);
         } finally {
             lock.unlock();
         }
    }

    public void replace(String serviceName, DrpcConnectionWrapper old, DrpcConnectionWrapper theNew) {
        try {
            lock.unlock();
            remove(serviceName, old);
            add(serviceName, theNew);
        } finally {
            lock.unlock();
        }
    }

    public void add(String serviceName, DrpcConnectionWrapper connectionWrapper) {
        try {
            serviceNames.add(serviceName);
            lock.lock();
            CopyOnWriteArrayList<DrpcConnectionWrapper> connectionWrappers =
                    originalConnectionCache.getOrDefault(serviceName, new CopyOnWriteArrayList<>());
            if (connectionWrappers.contains(connectionWrapper)) {
                return;
            }
            connectionWrappers.add(connectionWrapper);
            originalConnectionCache.put(serviceName, connectionWrappers);
        } finally {
            lock.lock();
        }
    }

    public List<DrpcConnectionWrapper> getOriginalConnections(String serviceName) {
        try {
            lock.lock();
            return originalConnectionCache.getOrDefault(serviceName, new CopyOnWriteArrayList<>());
        } finally {
            lock.unlock();
        }
    }

    public void setConnections(String serviceName, CopyOnWriteArrayList<DrpcConnectionWrapper> connectionWrappers) {
        try {
            serviceNames.add(serviceName);
            lock.lock();
            originalConnectionCache.put(serviceName, connectionWrappers);
        } finally {
            lock.unlock();
        }
    }

    public DrpcConnectionWrapper getConnectionWrapper(String serviceName, String ip, Integer port) {
        ChannelFuture channelFuture = connect(ip, port);
        DrpcConnectionWrapper connectionWrapper = new DrpcConnectionWrapper();
        connectionWrapper.setChannelFuture(channelFuture);
        connectionWrapper.setIp(ip);
        connectionWrapper.setPort(port);
        connectionWrapper.setServiceName(serviceName);
        return connectionWrapper;
    }

    public ChannelFuture connect(String ip, Integer port) {
        try {
            // 这里尝试重复获取连接，如果连接获取失败的话
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            if (channelFuture != null) {
                return channelFuture;
            }
            // 如果获取失败，那么这里尝试重复获取连接
            return RetryUtils.retry(retryConnectTimes, retryConnectInterval,
                    () -> bootstrap.connect(ip, port).sync(),
                    Objects::nonNull);
        } catch (Exception e) {
            logger.error("get connection failure ip {}, port {}, exception is {}", ip, port, e.getMessage());
        }
        return null;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public Integer getRetryConnectTimes() {
        return retryConnectTimes;
    }

    public void setRetryConnectTimes(Integer retryConnectTimes) {
        this.retryConnectTimes = retryConnectTimes;
    }

    public Long getRetryConnectInterval() {
        return retryConnectInterval;
    }

    public void setRetryConnectInterval(Long retryConnectInterval) {
        this.retryConnectInterval = retryConnectInterval;
    }
}
