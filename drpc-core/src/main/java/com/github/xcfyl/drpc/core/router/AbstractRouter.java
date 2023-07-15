package com.github.xcfyl.drpc.core.router;

import com.github.xcfyl.drpc.core.client.ConnectionManager;
import com.github.xcfyl.drpc.core.client.ConnectionWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 22:42
 */
@Slf4j
public abstract class AbstractRouter implements Router {
    protected final List<ConnectionWrapper> cache = new ArrayList<>();
    private final ConnectionManager connectionManager;

    public AbstractRouter(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public synchronized ConnectionWrapper select(String serviceName) throws Exception {
        ConnectionWrapper connectionWrapper = doSelect(serviceName);
        if (log.isDebugEnabled()) {
            log.debug("router -> {}, select connection is {}", this.getClass().getName(), connectionWrapper);
        }
        return connectionWrapper;
    }

    @Override
    public synchronized void refresh(String serviceName) {
        List<ConnectionWrapper> originalConnections = connectionManager.getOriginalConnections(serviceName);
        cache.clear();
        cache.addAll(originalConnections);
        doRefresh();
        log.debug("router refreshed -> #{}", cache);
    }

    /**
     * 子类进行cache的刷新
     */
    protected abstract void doRefresh();

    /**
     * 子类返回可用连接对象
     *
     * @param serviceName
     * @return
     */
    protected abstract ConnectionWrapper doSelect(String serviceName) throws Exception;
}
