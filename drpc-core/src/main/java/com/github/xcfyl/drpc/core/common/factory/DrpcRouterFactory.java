package com.github.xcfyl.drpc.core.common.factory;

import com.github.xcfyl.drpc.core.client.DprcConnectionManager;
import com.github.xcfyl.drpc.core.common.enums.DrpcRouterType;
import com.github.xcfyl.drpc.core.router.DrpcRandomRouter;
import com.github.xcfyl.drpc.core.router.DrpcRoundRobinRouter;
import com.github.xcfyl.drpc.core.router.DrpcRouter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:19
 */
@Slf4j
public class DrpcRouterFactory {
    public static DrpcRouter createRpcRouter(DrpcRouterType type, DprcConnectionManager connectionManager) {
        if (type == DrpcRouterType.RANDOM) {
            return new DrpcRandomRouter(connectionManager);
        } else if (type == DrpcRouterType.ROUND_ROBIN) {
            return new DrpcRoundRobinRouter(connectionManager);
        }
        throw new RuntimeException("暂不支持的路由类型");
    }
}
