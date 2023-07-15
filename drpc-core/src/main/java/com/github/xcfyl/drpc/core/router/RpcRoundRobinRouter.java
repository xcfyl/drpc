package com.github.xcfyl.drpc.core.router;

import com.github.xcfyl.drpc.core.client.ConnectionManager;
import com.github.xcfyl.drpc.core.client.ConnectionWrapper;

/**
 * 实现轮询策略
 *
 * @author 西城风雨楼
 * @date create at 2023/6/23 22:50
 */
public class RpcRoundRobinRouter extends RpcAbstractRouter {
    private int nextIndex;

    public RpcRoundRobinRouter(ConnectionManager connectionManager) {
        super(connectionManager);
        nextIndex = 0;
    }

    @Override
    public void doRefresh() {
        // 这里什么也不做
    }

    @Override
    public ConnectionWrapper doSelect(String serviceName) throws Exception {
        if (nextIndex == cache.size()) {
            nextIndex = 0;
        }
        return cache.get(nextIndex++);
    }
}
