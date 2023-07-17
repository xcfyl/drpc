package com.github.xcfyl.drpc.core.router;

import com.github.xcfyl.drpc.core.client.DrpcConnectionManager;
import com.github.xcfyl.drpc.core.client.DrpcConnectionWrapper;
import com.github.xcfyl.drpc.core.exception.DrpcRouterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 22:42
 */
public abstract class DrpcAbstractRouter implements DrpcRouter {
    private static final Logger logger = LoggerFactory.getLogger(DrpcAbstractRouter.class);

    protected final List<DrpcConnectionWrapper> cache = new ArrayList<>();
    private final DrpcConnectionManager connectionManager;

    public DrpcAbstractRouter(DrpcConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * select的基本作用是：
     * （1）过滤掉缓存中不正常的连接
     * （2）根据子类的select策略从cache中选择一个可用的连接返回给客户端
     *
     * @param serviceName
     * @return
     * @throws Exception
     */
    @Override
    public synchronized DrpcConnectionWrapper select(String serviceName) throws Exception {
        if (cache.size() == 0) {
            throw new DrpcRouterException("can't route, no connection found");
        }
        // 从缓存中移除连接已经不正常的连接
        cache.removeIf(connectionWrapper -> !connectionWrapper.isOk());
        DrpcConnectionWrapper connectionWrapper = doSelect(serviceName);
        if (connectionWrapper == null) {
            logger.error("no connection found");
            throw new DrpcRouterException("no connection found");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("router is {}, select connection is {}", getName(), connectionWrapper);
        }
        return connectionWrapper;
    }

    @Override
    public synchronized void refresh(String serviceName) {
        fillCache(serviceName, connectionManager);
        doRefresh();
        logger.debug("router refreshed, cache is {}", cache);
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    private void fillCache(String serviceName, DrpcConnectionManager connectionManager) {
        List<DrpcConnectionWrapper> originalConnections = connectionManager.getOriginalConnections(serviceName);
        cache.clear();
        cache.addAll(originalConnections);
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
    protected abstract DrpcConnectionWrapper doSelect(String serviceName) throws Exception;
}
