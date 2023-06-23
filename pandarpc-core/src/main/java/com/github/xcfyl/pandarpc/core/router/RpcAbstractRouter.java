package com.github.xcfyl.pandarpc.core.router;

import com.github.xcfyl.pandarpc.core.client.ConnectionManager;
import com.github.xcfyl.pandarpc.core.client.ConnectionWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 22:42
 */
@Slf4j
public abstract class RpcAbstractRouter implements RpcRouter {
    protected final List<ConnectionWrapper> cache = new ArrayList<>();

    @Override
    public synchronized ConnectionWrapper select(String serviceName) {
        return doSelect(serviceName);
    }

    @Override
    public synchronized void refresh(String serviceName) {
        List<ConnectionWrapper> connections = ConnectionManager.getConnections(serviceName);
        cache.clear();
        cache.addAll(connections);
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
    protected abstract ConnectionWrapper doSelect(String serviceName);
}
