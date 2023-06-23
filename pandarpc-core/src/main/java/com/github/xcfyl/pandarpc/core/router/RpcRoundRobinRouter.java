package com.github.xcfyl.pandarpc.core.router;

import com.github.xcfyl.pandarpc.core.client.ConnectionWrapper;

/**
 * 实现轮询策略
 *
 * @author 西城风雨楼
 * @date create at 2023/6/23 22:50
 */
public class RpcRoundRobinRouter extends RpcAbstractRouter {
    private int nextIndex;

    public RpcRoundRobinRouter() {
        nextIndex = 0;
    }

    @Override
    public void doRefresh() {
        // 这里什么也不做
    }

    @Override
    public ConnectionWrapper doSelect(String serviceName) {
        if (nextIndex == cache.size()) {
            nextIndex = 0;
        }
        return cache.get(nextIndex++);
    }
}
