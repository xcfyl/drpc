package com.github.xcfyl.drpc.core.router;

import com.github.xcfyl.drpc.core.client.DprcConnectionManager;
import com.github.xcfyl.drpc.core.client.DrpcConnectionWrapper;

/**
 * 实现轮询策略
 *
 * @author 西城风雨楼
 * @date create at 2023/6/23 22:50
 */
public class DrpcRoundRobinRouter extends DrpcAbstractRouter {
    private int nextIndex;

    public DrpcRoundRobinRouter(DprcConnectionManager connectionManager) {
        super(connectionManager);
        nextIndex = 0;
    }

    @Override
    public void doRefresh() {
        // 这里什么也不做
    }

    @Override
    public DrpcConnectionWrapper doSelect(String serviceName) throws Exception {
        if (nextIndex == cache.size()) {
            nextIndex = 0;
        }
        return cache.get(nextIndex++);
    }
}
