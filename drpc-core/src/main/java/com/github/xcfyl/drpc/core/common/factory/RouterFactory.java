package com.github.xcfyl.drpc.core.common.factory;

import com.github.xcfyl.drpc.core.client.ConnectionManager;
import com.github.xcfyl.drpc.core.common.enums.RouterType;
import com.github.xcfyl.drpc.core.router.RandomRouter;
import com.github.xcfyl.drpc.core.router.RoundRobinRouter;
import com.github.xcfyl.drpc.core.router.Router;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:19
 */
@Slf4j
public class RouterFactory {
    public static Router createRpcRouter(RouterType type, ConnectionManager connectionManager) {
        if (type == RouterType.RANDOM) {
            return new RandomRouter(connectionManager);
        } else if (type == RouterType.ROUND_ROBIN) {
            return new RoundRobinRouter(connectionManager);
        }
        throw new RuntimeException("暂不支持的路由类型");
    }
}
