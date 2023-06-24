package com.github.xcfyl.pandarpc.core.router;

import com.github.xcfyl.pandarpc.core.client.ConnectionWrapper;
import com.github.xcfyl.pandarpc.core.exception.RouterException;

import java.util.Collections;
import java.util.Random;

/**
 * 实现随机路由策略
 *
 * @author 西城风雨楼
 * @date create at 2023/6/23 22:37
 */
public class RpcRandomRouter extends RpcAbstractRouter {
    @Override
    public void doRefresh() {
        // 将cache中的数据进行扰乱
        Collections.shuffle(cache);
    }

    @Override
    public ConnectionWrapper doSelect(String serviceName) throws Exception {
        Random random = new Random();
        if (cache.size() == 0) {
            throw new RouterException("no router found!");
        }
        int index = random.nextInt(cache.size());
        return cache.get(index);
    }
}
